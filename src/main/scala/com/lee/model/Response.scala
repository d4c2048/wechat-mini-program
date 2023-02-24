package com.lee.model

sealed trait Response extends Model {
  def res: String

  def statusCode: Int

  def msg: String
}

final case class SuccessResponse(override val msg: String = "", override val statusCode: Int = 20000, override val res: String = "") extends Response

final case class FailureResponse(override val msg: String = "", override val statusCode: Int = 50000, override val res: String = "") extends Response
