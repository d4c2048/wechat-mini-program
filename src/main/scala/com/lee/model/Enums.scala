package com.lee.model

sealed trait Enum extends Model

/**
 * 账号状态枚举，
 * 枚举值:0 -> 下线
 * 1 -> 在线
 */
sealed trait StudentStatus {
  def code: Int
}

object StudentStatus {
  object OFFLINE extends StudentStatus {
    override val code = 0
  }

  object ONLINE extends StudentStatus {
    override val code = 1
  }

  def toStatus(num: Int): StudentStatus = {
    num match {
      case 0 => OFFLINE
      case 1 => ONLINE
      case _ => throw new IllegalArgumentException
    }
  }
}

/**
 * 账号身份枚举，
 * 枚举值:0 -> 学生
 * 1 -> 志愿者
 */
sealed trait Role {
  def code: Int
}

object Role {
  object STUDENT extends Role {
    override val code = 0
  }

  object VOLUNTEER extends Role {
    override val code = 1
  }

  def toRole(num: Int): Role = {
    num match {
      case 0 => STUDENT
      case 1 => VOLUNTEER
      case _ => throw new IllegalArgumentException
    }
  }
}

/**
 * 委托状态枚举，
 * 枚举值:<br>
 * -1 -> 已撤销<br>
 * 0 -> 已完成<br>
 * 1 -> 已送达<br>
 * 2 -> 已承接<br>
 * 3 -> 未承接<br>
 * 4 -> 超一天未被承接<br>
 * 5 -> 超三天未被承接
 */
sealed trait OrderStatus {
  def code: Int
}

object OrderStatus {
  object CANCELLED extends OrderStatus {
    override val code = -1
  }

  object CONFIRM_RECEIPT extends OrderStatus {
    override val code = 0
  }

  object DELIVERED extends OrderStatus {
    override val code = 1
  }

  object RECEIVED extends OrderStatus {
    override val code = 2
  }

  object NO_CLAIMED extends OrderStatus {
    override val code = 3
  }

  object OVER_ONE_DAY extends OrderStatus {
    override val code = 4
  }

  object OVER_THREE_DAYS extends OrderStatus {
    override val code = 5
  }

  def toStatus(num: Int): OrderStatus = {
    num match {
      case -1 => CANCELLED
      case 0 => CONFIRM_RECEIPT
      case 1 => RECEIVED
      case 2 => DELIVERED
      case 3 => NO_CLAIMED
      case 4 => OVER_ONE_DAY
      case 5 => OVER_THREE_DAYS
      case _ => throw new IllegalArgumentException
    }
  }
}
