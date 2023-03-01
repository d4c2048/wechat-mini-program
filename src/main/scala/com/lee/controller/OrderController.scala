package com.lee.controller

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.FileInfo
import akka.util.Timeout
import cn.hutool.core.util.ObjectUtil
import com.lee.controller.OrderController.{getOdrId, renameFile}
import com.lee.controller.directive.AuthDirective
import com.lee.model.json.EntityJsonProtocol
import com.lee.model.{FailureResponse, GetAllNoClaimOrder, GetOrderById, Order, OrderCommand, OrderStatus, SaveOrder, Student, SuccessResponse, UpdateOrderStatus}
import com.lee.util.DateUtil
import spray.json._

import java.io.File
import java.text.DecimalFormat
import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.{Failure, Success}

class OrderController(odrActor: ActorRef[OrderCommand])(implicit system: ActorSystem[_])
                                                          extends AuthDirective
                                                          with EntityJsonProtocol {
  def route: Route = {
    implicit val scheduler = system.scheduler
    auth(stuJson => {
      val stu = stuJson
        .parseJson
        .convertTo[Student]
      pathPrefix("order") {
        pathEndOrSingleSlash {

          // 发布委托接口
          post {
            formField("address", "remarks")((address, remarks) => {
              val id = getOdrId
              val odr = Order(id, stu.stuId, 0, OrderStatus.NO_CLAIMED.code, address, DateUtil.currentTime, remarks)
              storeUploadedFile("screen_shot", renameFile(id))((_, _) => {
                implicit val timeout = Timeout(3 seconds)
                val future = odrActor ? (replyTo => SaveOrder(odr, replyTo))
                onComplete(future) {
                  case Success(value) if value =>
                    complete(SuccessResponse("发布成功"))
                  case Success(value) if !value =>
                    complete(FailureResponse("发布失败"))
                  case Failure(_) =>
                    complete(FailureResponse("服务器错误：500"))
                }
              })
            })
          } ~

          // 获取还未被承接的所有订单接口
          get {
            implicit val timeout = Timeout(3 seconds)
            val future = odrActor ? GetAllNoClaimOrder.apply
            onComplete(future) {
              case Success(list) if list.isEmpty =>
                complete(SuccessResponse("暂时还没有委托", 20001))
              case Success(list) =>
                complete(SuccessResponse(res = list.toJson.compactPrint))
              case Failure(_) =>
                complete(FailureResponse("服务器错误：500"))
            }
          }
        } ~
        path(Segment)(odrId => {
          // 通过委托Id获取委托实体
          get {
            implicit val timeout = Timeout(3 seconds)
            val future = odrActor ? (replyTo => GetOrderById(odrId, replyTo))
            onComplete(future) {
              case Success(odr) if ObjectUtil.isNull(odr) =>
                complete(SuccessResponse("委托号错误", 20001))
              case Success(odr) =>
                complete(SuccessResponse(res = odr.toJson.compactPrint))
              case Failure(_) =>
                complete(FailureResponse("服务器错误：500"))
            }
          } ~

          // 根据委托Id修改委托状态
          (put & parameter("status".as[Int]))(s => {
            implicit val timeout = Timeout(3 seconds)
            val status = OrderStatus.toStatus(s)
            val future = odrActor ? (replyTo => UpdateOrderStatus(odrId, status, replyTo))
            onComplete(future) {
              case Success(value) if value =>
                complete(SuccessResponse("修改成功"))
              case Success(odr) =>
                complete(FailureResponse("修改失败"))
              case Failure(_) =>
                complete(FailureResponse("服务器错误：500"))
            }
          })
        })
      }
    })
  }
}

object OrderController {
  private[this] val id = new AtomicInteger(1)

  def apply(odrActor: ActorRef[OrderCommand])(implicit system: ActorSystem[_]): OrderController = new OrderController(odrActor)

  def getOdrId: String = {
    val head = "ORDER"
    val date = DateUtil.currentTime("yyyyMMddHHmm")
    val id = new DecimalFormat("00000").format(this.id.getAndIncrement)
    head + date + id
  }

  def renameFile(newName: String)(fileInfo: FileInfo): File = {
    val suffix = fileInfo.fileName.substring(fileInfo.fileName.lastIndexOf("."))
    new File(s"$resourcesPath/screen_shot/${newName + suffix}")
  }
}
