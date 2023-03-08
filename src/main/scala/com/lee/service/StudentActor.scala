package com.lee.service

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.scaladsl.Behaviors
import akka.util.Timeout
import cn.hutool.core.util.ObjectUtil
import com.lee.key
import com.lee.model.json.EntityJsonProtocol
import com.lee.model.{AddStudent, FailureResponse, StudentCommand, StudentStatus, SuccessResponse, UpdatePwd, UpdateStudentRole, UpdateStudentStatus, VerifyLogin}
import com.lee.util.JwtUtil
import spray.json._

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object StudentActor extends EntityJsonProtocol {
  def apply(): Behavior[StudentCommand] = {
    Behaviors.setup(ctx => {
      val self = ctx.self
      implicit val scheduler = ctx.system.scheduler
      Behaviors.receiveMessagePartial {
        case VerifyLogin(stuInfo, replyTo) =>
          val student = stuDao.authStudent(stuInfo)
          if (ObjectUtil.equal(student.stuId, 0L)) {
            replyTo ! ""
          } else {
            student.pwd = ""
            val token = JwtUtil.generateToken(null, Map("student" -> student.toJson.compactPrint), key)
            replyTo ! token
            implicit val timeout = Timeout(3 seconds)
            self ? (UpdateStudentStatus(student.stuId, StudentStatus.ONLINE, _))
          }
          Behaviors.same

        case AddStudent(stu, replyTo) =>
          replyTo ! stuDao.addStudent(stu)
          Behaviors.same

        case UpdateStudentStatus(stuId, status, replyTo) =>
          replyTo ! stuDao.updateStatusById(stuId, status)
          Behaviors.same

        case UpdatePwd(stuId, pwd, replyTo) =>
          replyTo ! stuDao.updatePwdById(stuId, pwd)
          Behaviors.same

        case UpdateStudentRole(stuId, role, replyTo) =>
          replyTo ! stuDao.updateRoleById(stuId, role)
          Behaviors.same
      }
    })
  }
}
