package com.lee.controller

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import cn.hutool.core.util.ObjectUtil
import com.lee.model.{FailureResponse, LoginStudent, StudentCommand, SuccessResponse, VerifyLogin}
import com.lee.model.json.StudentJsonProtocol
import de.heikoseeberger.akkahttpjackson.JacksonSupport
import spray.json._

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.{Failure, Success}

class StudentController(studentActor: ActorRef[StudentCommand])(implicit system: ActorSystem[_]) extends JacksonSupport with StudentJsonProtocol {
  private implicit val scheduler = system.scheduler

  def route: Route = {
    pathPrefix("student") {
      (path("login") & post & entity(as[LoginStudent]))(loginInfo => {
        implicit val timeout = Timeout(3 seconds)
        val askFuture = studentActor ? (replyTo => VerifyLogin(loginInfo, replyTo))
        onComplete(askFuture) {
          case Success(student) =>
            if (ObjectUtil.isNull(student)) complete(FailureResponse("请检查用户名和密码。"))
            else complete(SuccessResponse(res = student.toJson.compactPrint))
          case Failure(_) => complete(FailureResponse("服务器错误：500 error."))
        }
      })
    }
  }
}

object StudentController {
  def apply(studentActor: ActorRef[StudentCommand])(implicit system: ActorSystem[_]): StudentController = new StudentController(studentActor)
}
