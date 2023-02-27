package com.lee.model

import akka.actor.typed.ActorRef

sealed trait Command extends Model

sealed trait StudentCommand extends Command

final case class VerifyLogin(stuInfo: LoginStudent, replyTo: ActorRef[Student]) extends StudentCommand

final case class AddStudent(stu: Student, replyTo: ActorRef[Response]) extends StudentCommand

final case class UpdateStudentStatus(stuId: Long, status: StudentStatus, replyTo: ActorRef[Response]) extends StudentCommand

final case class UpdatePwd(stuId: Long, pwd: String, replyTo: ActorRef[Response]) extends StudentCommand

final case class UpdateStudentRole(stuId: Long, role: Role, replyTo: ActorRef[Response]) extends StudentCommand

sealed trait OrderCommand extends Command

case object CheckOrderStatus extends OrderCommand

final case class UpdateOrderStatus(odrId: String, status: OrderStatus, replyTo: ActorRef[Boolean]) extends OrderCommand

final case class AddOrder(odr: Order, replyTo: ActorRef[Response]) extends OrderCommand