package com.lee.service

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.scaladsl.Behaviors
import akka.util.Timeout
import cn.hutool.core.util.ObjectUtil
import com.lee.model.{AddOrder, CheckOrderStatus, FailureResponse, OrderCommand, OrderStatus, Response, SuccessResponse, UpdateOrderStatus}
import com.lee.util.DateUtil

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.{Failure, Success}

object OrderActor {
  def apply(): Behavior[OrderCommand] = {
    Behaviors.setup(ctx => {
      val self = ctx.self
      val log = ctx.log
      implicit val scheduler = ctx.system.scheduler
      implicit val timeout = Timeout(1 second)
      implicit val executionContext = ctx.executionContext
      Behaviors.withTimers(timer => {
        timer.startTimerAtFixedRate(CheckOrderStatus, CheckOrderStatus, 1 minute)
        Behaviors.receiveMessagePartial {
          case CheckOrderStatus =>
            odrDao
              .getAllCheckOrder
              .foreach(odr => {
                val createDate = DateUtil.parseDate(odr.date)
                var future: Future[Boolean] = null
                if (DateUtil.overSomeDays(createDate, 3)) {
                  future = self ? (replyTo => UpdateOrderStatus(odr.odrId, OrderStatus.OVER_THREE_DAYS, replyTo))
                } else if (DateUtil.overOneDay(createDate)) {
                  future = self ? (replyTo => UpdateOrderStatus(odr.odrId, OrderStatus.OVER_ONE_DAY, replyTo))
                }
                if (ObjectUtil.isNotNull(future)) {
                  future.onComplete {
                    case Failure(exception) =>
                      log.error(s"Update order(id: ${odr.odrId}) status FAILED with a EXCEPTION: ${exception.getMessage}")
                    case Success(value) if !value => log.warn(s"Update order(id: ${odr.odrId}) status FAILED!")
                    case Success(value) if value => log.debug(s"Update order(id: ${odr.odrId}) status SUCCEED!")
                  }
                }
              })
            Behaviors.same

          case UpdateOrderStatus(odrId, status, replyTo) =>
            replyTo ! odrDao.updateOrderStatusById(odrId, status)
            Behaviors.same

          case AddOrder(odr, replyTo) =>
            val res = odrDao.addOrder(odr)
            replyTo ! (if (res) SuccessResponse("新建委托成功") else FailureResponse("新建委托失败"))
            Behaviors.same
        }
      })
    })
  }
}
