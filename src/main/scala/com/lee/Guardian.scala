package com.lee

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object Guardian {
  def apply(): Behavior[Nothing] = {
    Behaviors.setup[Nothing](ctx => {
      Behaviors.same
    })
  }
}
