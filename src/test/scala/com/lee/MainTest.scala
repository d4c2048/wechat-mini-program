package com.lee

import com.lee.model.Order
import com.lee.util.JwtUtil
import org.springframework.data.mongodb.core.{MongoTemplate, SimpleMongoClientDatabaseFactory}

import scala.jdk.CollectionConverters._

case class User(user_id: String, user_name: String, var age: Int, address: String)

object MainTest {
  val mongodbTemplate = new MongoTemplate(new SimpleMongoClientDatabaseFactory("mongodb://localhost:27017/test_db"))

  def main(args: Array[String]): Unit = {
    //mongodbTemplate.findOne()
  }
}
