package com.lee

import com.lee.model.Order
import com.lee.util.JwtUtil
import org.springframework.data.mongodb.core.{MongoTemplate, SimpleMongoClientDatabaseFactory}

import scala.jdk.CollectionConverters._

case class User(user_id: String, user_name: String, age: Int, adress: String)

object MainTest {
  def main(args: Array[String]): Unit = {
    classOf[Order].getDeclaredFields.map(_.getName).foreach(println)
  }
}
