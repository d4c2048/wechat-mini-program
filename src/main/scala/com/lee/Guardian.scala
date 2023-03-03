package com.lee

import akka.actor.typed.{Behavior, SupervisorStrategy}
import akka.actor.typed.scaladsl.Behaviors
import com.lee.controller.ApiService
import com.lee.service.{OrderActor, StudentActor}

object Guardian {
  def apply(): Behavior[Nothing] = {
    Behaviors.setup[Nothing](ctx => {
      implicit val system = ctx.system
      val config = system.settings.config
      key = config.getString("jwt.key")
      val stuActorBehavior = Behaviors
        .supervise(StudentActor())
        .onFailure(SupervisorStrategy.restart)
      val stuActor = ctx.spawn(stuActorBehavior, "stuActor")
      val odrActorBehavior = Behaviors
        .supervise(OrderActor())
        .onFailure(SupervisorStrategy.restart)
      val odrActor = ctx.spawn(odrActorBehavior, "odrActor")
      val route = ApiService(stuActor, odrActor).route
      WebServer.start(route)
      Behaviors.empty
    })
  }
}
