package com.lee.service

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import cn.hutool.core.util.ObjectUtil
import com.lee.model.{AddStudent, FailureResponse, StudentCommand, SuccessResponse, UpdatePwd, UpdateStudentRole, UpdateStudentStatus, VerifyLogin}

import scala.language.postfixOps

object StudentActor {
  def apply(): Behavior[StudentCommand] = {
    Behaviors.setup(_ => {
      Behaviors.receiveMessagePartial {
        case VerifyLogin(stuInfo, replyTo) =>
          val student = stuDao.authStudent(stuInfo)
          if (ObjectUtil.isNotNull(student)) student.pwd = ""
          replyTo ! student
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
