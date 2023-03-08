package com.lee.controller

import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import cn.hutool.core.util.ObjectUtil
import com.lee.controller.directive.AuthDirective
import com.lee.model._
import com.lee.model.json.EntityJsonProtocol
import de.heikoseeberger.akkahttpjackson.JacksonSupport
import spray.json._

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.{Failure, Success}

class StudentController(stuActor: ActorRef[StudentCommand])(implicit system: ActorSystem[_])
                                                               extends JacksonSupport
                                                               with EntityJsonProtocol
                                                               with AuthDirective {
  private implicit val scheduler = system.scheduler
  private val log = system.log

  def route: Route = {
    pathPrefix("student") {
      // 用户注册接口
      (path("registry") & post & entity(as[Student]))(stu => {
        implicit val timeout = Timeout(3 seconds)
        val future = stuActor ? (AddStudent(stu, _))
        onComplete(future) {
          case Success(value) if value => complete(SuccessResponse("注册成功"))
          case Success(_) => complete(FailureResponse("注册失败"))
          case Failure(e) =>
            log.error(e.getMessage)
            complete(FailureResponse("服务器错误：500 error."))
        }
      }) ~
      // 用户登录接口
      (path("login") & post & entity(as[LoginStudent]))(loginInfo => {
        implicit val timeout = Timeout(3 seconds)
        val askFuture = stuActor ? (replyTo => VerifyLogin(loginInfo, replyTo))
        onComplete(askFuture) {
          case Success(token) =>
            if (ObjectUtil.equal(token, "")) complete(FailureResponse("请检查用户名和密码。"))
            else complete(SuccessResponse(res = token))
          case Failure(e) =>
            log.error(e.getMessage)
            complete(FailureResponse("服务器错误：500 error."))
        }
      }) ~
      auth(stuJson => {
        val stu = stuJson
          .parseJson
          .convertTo[Student]

        // 获取安全问题接口
        path("secure") {
            complete(SuccessResponse(res = stu.secure.toJson.compactPrint))
        } ~
        // 验证安全问题接口
        (path("verify" / "secure") & post & entity(as[Map[String, String]]))(answer => {
          complete(if (answer == stu.secure.parseJson.convertTo[Map[String, String]]) SuccessResponse() else FailureResponse())
        }) ~
        // 修改密码接口
        (path("pwd") & put & entity(as[String]))(pwd => {
          implicit val timeout = Timeout(3 seconds)
          val future = stuActor ? (replyTo => UpdatePwd(stu.stuId, pwd, replyTo))
          onComplete(future) {
            case Success(value) if value =>
              complete(SuccessResponse("更新成功"))
            case Success(value) if !value =>
              complete(FailureResponse("更新失败"))
            case Failure(_) =>
              complete(FailureResponse("服务器错误：500"))
          }
        })
      })
    }
  }
}

object StudentController {
  def apply(studentActor: ActorRef[StudentCommand])
           (implicit system: ActorSystem[_]): StudentController = {
    new StudentController(studentActor)
  }
}
