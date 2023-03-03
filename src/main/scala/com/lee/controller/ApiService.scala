package com.lee.controller

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.lee.model.{OrderCommand, StudentCommand}

class ApiService(stuActor: ActorRef[StudentCommand], odrActor: ActorRef[OrderCommand])
                (implicit system: ActorSystem[_]) {
  val route: Route =
    StudentController(stuActor).route ~
    OrderController(odrActor).route
}

object ApiService {
  def apply(stuActor: ActorRef[StudentCommand], odrActor: ActorRef[OrderCommand])
           (implicit system: ActorSystem[_]): ApiService = {
    new ApiService(stuActor, odrActor)
  }
}
