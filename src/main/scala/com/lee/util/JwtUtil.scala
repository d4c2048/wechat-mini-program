package com.lee.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT

import java.util.{Base64, Calendar}
import scala.jdk.CollectionConverters._

object JwtUtil {
  def generateToken(header: Map[String, AnyRef], payload: Map[String, AnyRef], key: String): String = {
    val encodeKey = Base64.getEncoder.encode(key.getBytes)
    val date = Calendar.getInstance.getTime
    val nextDayMill = date.getTime + 24 * 60 * 60 * 1000
    date.setTime(nextDayMill)
    JWT
      .create
      .withHeader(header.asJava)
      .withPayload(payload.asJava)
      .withExpiresAt(date)
      .sign(Algorithm.HMAC256(encodeKey))
  }

  def verifyToken(token: String, key: String): DecodedJWT = {
    val encodeKey = Base64.getEncoder.encode(key.getBytes)
    val verifier = JWT.require(Algorithm.HMAC256(encodeKey)).build
    try {
      verifier.verify(token)
    } catch {
      case _: Exception => null
    }
  }
}
