package com.lee.model

import akka.actor.typed.ActorRef

sealed trait Command extends Model

sealed trait StudentCommand extends Command

final case class VerifyLogin(stuInfo: LoginStudent, replyTo: ActorRef[Student]) extends StudentCommand