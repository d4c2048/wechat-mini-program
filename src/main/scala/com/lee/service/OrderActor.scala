package com.lee.service

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.scaladsl.Behaviors
import akka.util.Timeout
import cn.hutool.core.util.ObjectUtil
import com.lee.model._
import com.lee.util.DateUtil

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.Future
import scala.concurrent.duration.{DurationInt, DurationLong}
import scala.language.postfixOps
import scala.util.{Failure, Success}

object OrderActor {
  private[this] val id = new AtomicInteger(1)

  def apply(): Behavior[OrderCommand] = {
    Behaviors.setup(ctx => {
      val self = ctx.self
      val log = ctx.log
      implicit val scheduler = ctx.system.scheduler
      implicit val timeout = Timeout(1 second)
      implicit val executionContext = ctx.executionContext
      Behaviors.withTimers(timer => {
        timer.startTimerAtFixedRate(CheckOrderStatus, CheckOrderStatus, 1 minute)
        timer.startTimerAtFixedRate(ResetId, ResetId, DateUtil.diffHour() nanos, 1 day)
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
                    case Success(value) if !value =>
                      log.warn(s"Update order(id: ${odr.odrId}) status FAILED!")
                    case Success(_) =>
                      log.debug(s"Update order(id: ${odr.odrId}) status SUCCEED!")
                  }
                }
              })
            Behaviors.same

          case UpdateOrderStatus(odrId, status, replyTo) =>
            replyTo ! odrDao.updateOrderStatusById(odrId, status)
            Behaviors.same

          case AddOrder(odr, replyTo) =>
              replyTo ! odrDao.addOrder(odr)
            Behaviors.same

          case GetAllNoClaimOrder(replyTo) =>
            replyTo ! odrDao.getAllNoClaimOrder
            Behaviors.same

          case ResetId =>
            id.set(1)
            Behaviors.same

          case SaveOrder(odr, replyTo) =>
            replyTo ! odrDao.addOrder(odr)
            Behaviors.same

          case GetOrderById(odrId, replyTo) =>
            replyTo ! odrDao.getOrderById(odrId)
            Behaviors.same

          case GetStudentOrders(stuId, replyTo) =>
            replyTo ! odrDao.getAllOrderByStuId(stuId)
            Behaviors.same
        }
      })
    })
  }
}
