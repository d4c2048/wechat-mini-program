package com.lee

import org.springframework.data.mongodb.core.{MongoTemplate, SimpleMongoClientDatabaseFactory}

import scala.jdk.CollectionConverters._

case class User(user_id: String, user_name: String, age: Int, adress: String)

object MainTest {
  def main(args: Array[String]): Unit = {
/*    val builder = new MongoClientOptions.Builder()
    builder.maxWaitTime(1000 * 60 * 3)
    builder.connectTimeout(1000 * 60 * 3)
    builder.minConnectionsPerHost(1)*/
    val mongoDbFactory = new SimpleMongoClientDatabaseFactory("mongodb://localhost:27017/test_db")
    val template = new MongoTemplate(mongoDbFactory)
    println(template.findAll(classOf[User], "user").asScala)
  }
}
