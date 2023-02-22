package com.lee

import akka.actor.typed.ActorSystem

object Start {
  def main(args: Array[String]): Unit = {
    ActorSystem[Nothing](Guardian(), "system")
  }
}
