package com.lee.model

sealed trait Entity extends Model

final case class Student(stuId: Long, stuName: String, role: Int, var pwd: String, status: Int) extends Entity

final case class LoginStudent(stuId: Long, pwd: String) extends Entity

final case class Order(odrId: String, stuId: Long, vtId: Long, status: Int, address: String) extends Entity