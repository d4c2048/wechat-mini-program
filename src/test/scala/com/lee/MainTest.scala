package com.lee

import com.lee.model.Order
import com.lee.util.JwtUtil
import org.springframework.data.mongodb.core.{MongoTemplate, SimpleMongoClientDatabaseFactory}

import scala.jdk.CollectionConverters._

case class User(user_id: String, user_name: String, var age: Int, address: String)

object MainTest {
  def partialFunc(num: Int): String = {
    case 0 => "零"
    case 1 => "一"
  }

  def main(args: Array[String]): Unit = {
    println(partialFunc(0))
  }
}
