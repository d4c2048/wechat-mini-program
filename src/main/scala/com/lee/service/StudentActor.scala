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
          val res = stuDao.addStudent(stu)
          replyTo ! (if (res) SuccessResponse("注册成功") else FailureResponse("注册失败"))
          Behaviors.same

        case UpdateStudentStatus(stuId, status, replyTo) =>
          val res = stuDao.updateStatusById(stuId, status)
          replyTo ! (if (res) SuccessResponse() else FailureResponse())
          Behaviors.same

        case UpdatePwd(stuId, pwd, replyTo) =>
          val res = stuDao.updatePwdById(stuId, pwd)
          replyTo ! (if (res) SuccessResponse("修改密码成功") else FailureResponse("修改密码失败"))
          Behaviors.same

        case UpdateStudentRole(stuId, role, replyTo) =>
          val res = stuDao.updateRoleById(stuId, role)
          replyTo ! (if (res) SuccessResponse("更新成功") else FailureResponse("更新失败"))
          Behaviors.same
      }
    })
  }
}
