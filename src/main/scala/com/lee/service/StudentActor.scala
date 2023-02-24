package com.lee.service

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import cn.hutool.core.util.ObjectUtil
import com.lee.model.{StudentCommand, VerifyLogin}

object StudentActor {
  def apply(): Behavior[StudentCommand] = {
    Behaviors.setup(_ => {
      Behaviors.receiveMessagePartial {
        case VerifyLogin(stuInfo, replyTo) =>
          val student = stuDao.authStudent(stuInfo)
          if (ObjectUtil.isNotNull(student)) student.pwd = ""
          replyTo ! student
          Behaviors.same
      }
    })
  }
}
