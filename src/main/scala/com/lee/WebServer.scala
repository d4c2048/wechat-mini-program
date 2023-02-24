package com.lee

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.{Failure, Success}

object WebServer {
  private val HOST_PATH = "akka.http.host"
  private val PORT_PATH = "akka.http.port"

  def start(route: Route)(implicit system: ActorSystem[_]): Unit = {
    implicit val executionContext = system.executionContext
    val log = system.log
    val config = system.settings.config
    val host = config.getString(HOST_PATH)
    val port = config.getInt(PORT_PATH)
    Http()
      .newServerAt(host, port)
      .bind(route)
      .map(_.addToCoordinatedShutdown(3 seconds))
      .onComplete {
        case Success(binding) =>
          val (host, port) = (binding.localAddress.getHostString, binding.localAddress.getPort)
          log.info(s"Server at http://$host:$port")
        case Failure(exception) =>
          log.error("Server started failed with exception:\n")
          exception.printStackTrace()
      }
  }
}
