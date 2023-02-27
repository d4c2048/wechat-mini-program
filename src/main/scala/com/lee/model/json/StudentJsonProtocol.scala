package com.lee.model.json

import com.lee.model.{LoginStudent, Order, Student}
import spray.json.DefaultJsonProtocol

trait StudentJsonProtocol extends DefaultJsonProtocol {
  implicit val loginStudentJsonProtocol = jsonFormat2(LoginStudent)
  implicit val studentJsonProtocol = jsonFormat6(Student)
  implicit val oderJsonProtocol = jsonFormat7(Order)
}
