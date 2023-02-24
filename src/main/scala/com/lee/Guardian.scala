package com.lee

import akka.actor.typed.{Behavior, SupervisorStrategy}
import akka.actor.typed.scaladsl.Behaviors
import com.lee.controller.ApiService
import com.lee.service.StudentActor

object Guardian {
  def apply(): Behavior[Nothing] = {
    Behaviors.setup[Nothing](ctx => {
      implicit val system = ctx.system
      val stuActorBehavior = Behaviors
        .supervise(StudentActor())
        .onFailure(SupervisorStrategy.restart)
      val stuActor = ctx.spawn(stuActorBehavior, "stuActor")
      val route = ApiService(stuActor).route
      WebServer.start(route)
      Behaviors.empty
    })
  }
}
