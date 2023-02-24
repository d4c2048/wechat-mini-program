package com.lee.model.json

import com.lee.model.{LoginStudent, Student}
import spray.json.DefaultJsonProtocol

trait StudentJsonProtocol extends DefaultJsonProtocol {
  implicit val loginStudentJsonProtocol = jsonFormat2(LoginStudent)
  implicit val studentJsonProtocol = jsonFormat5(Student)
}
