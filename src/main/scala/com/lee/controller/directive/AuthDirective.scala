package com.lee.controller.directive


import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directive1
import cn.hutool.core.util.ObjectUtil
import com.lee.key
import com.lee.model.json.StudentJsonProtocol
import com.lee.model.{FailureResponse, Student}
import com.lee.util.JwtUtil
import de.heikoseeberger.akkahttpjackson.JacksonSupport
import spray.json._

trait AuthDirective extends JacksonSupport with StudentJsonProtocol {
  def auth: Directive1[Student] = {
    parameter("token").flatMap(token => {
      val jwt = JwtUtil.verifyToken(token, key)
      if (ObjectUtil.isNull(jwt)) complete(FailureResponse("token已过期"))
      else {
        val stu = jwt
          .getClaim("student")
          .asString
          .parseJson
          .convertTo[Student]
        provide(stu)
      }
    })
  }
}
