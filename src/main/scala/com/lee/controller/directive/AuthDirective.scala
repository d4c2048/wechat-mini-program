package com.lee.controller.directive


import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives._
import cn.hutool.core.util.ObjectUtil
import com.lee.key
import com.lee.model.{FailureResponse, Student}
import com.lee.util.JwtUtil
import de.heikoseeberger.akkahttpjackson.JacksonSupport

trait AuthDirective extends JacksonSupport {
  def auth: Directive1[String] = {
    parameter("token").flatMap(token => {
      val jwt = JwtUtil.verifyToken(token, key)
      if (ObjectUtil.isNull(jwt)) complete(FailureResponse("token已过期"))
      else {
        val stu = jwt
          .getClaim("student")
          .asString
        provide(stu)
      }
    })
  }
}
