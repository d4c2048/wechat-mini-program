package com.lee.model

import akka.actor.typed.ActorRef

sealed trait Command extends Model

sealed trait StudentCommand extends Command

final case class VerifyLogin(stuInfo: LoginStudent, replyTo: ActorRef[Student]) extends StudentCommand

final case class AddStudent(stu: Student, replyTo: ActorRef[Boolean]) extends StudentCommand

final case class UpdateStudentStatus(stuId: Long, status: StudentStatus, replyTo: ActorRef[Boolean]) extends StudentCommand

final case class UpdatePwd(stuId: Long, pwd: String, replyTo: ActorRef[Boolean]) extends StudentCommand

final case class UpdateStudentRole(stuId: Long, role: Role, replyTo: ActorRef[Boolean]) extends StudentCommand

sealed trait OrderCommand extends Command

case object CheckOrderStatus extends OrderCommand

case object ResetId extends OrderCommand

final case class UpdateOrderStatus(odrId: String, status: OrderStatus, replyTo: ActorRef[Boolean]) extends OrderCommand

final case class AddOrder(odr: Order, replyTo: ActorRef[Boolean]) extends OrderCommand

final case class GetAllNoClaimOrder(replyTo: ActorRef[List[Order]]) extends OrderCommand

final case class SaveOrder(odr: Order, replyTo: ActorRef[Boolean]) extends OrderCommand

final case class GetOrderById(odrId: String, replyTo: ActorRef[Order]) extends OrderCommand

final case class GetStudentOrders(stuId: Long, replyTo: ActorRef[List[Order]]) extends OrderCommand