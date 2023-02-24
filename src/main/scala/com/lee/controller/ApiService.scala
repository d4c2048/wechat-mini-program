package com.lee.controller

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Route
import com.lee.model.StudentCommand

class ApiService(stuActor: ActorRef[StudentCommand])(implicit system: ActorSystem[_]) {
  val route: Route =
    StudentController(stuActor).route
}

object ApiService {
  def apply(stuActor: ActorRef[StudentCommand])(implicit system: ActorSystem[_]): ApiService = new ApiService(stuActor)
}
