package com.lee

import com.lee.util.JwtUtil
import org.springframework.data.mongodb.core.{MongoTemplate, SimpleMongoClientDatabaseFactory}

import scala.jdk.CollectionConverters._

case class User(user_id: String, user_name: String, age: Int, adress: String)

object MainTest {
  def main(args: Array[String]): Unit = {
    val str = JwtUtil.generateToken(null, Map("name" -> "sdasd", "age" -> new Integer(18)), "Keydasdasdweretstg sertg hnhesdfg azwerf rag hbwert43543 56wyhfdsgeawrf")
    val jwt = JwtUtil.verifyToken(str, "Keydasdasdweretstg sertg hhesdfg azwerf rag hbwert43543 56wyhfdsgeawrf")
    if (jwt == null) println("非法") else println("合法")
  }
}
