package com.lee.model

sealed trait Entity extends Model

final case class Student(stuId: Long, stuName: String, role: Int, var pwd: String, status: Int) extends Entity

final case class LoginStudent(stuId: Long, pwd: String) extends Entity

/**
 * 快递代送委托实体
 * @param odrId 委托Id
 * @param stuId 委托人Id
 * @param vtId 志愿者Id
 * @param status 委托状态；
 *               -1：已取消
 *               0：已确认收货
 *               1：已送达
 *               2：已被承接，但未还未送达
 *               3：未承接
 *               4：超一天未承接
 *               5：超三天未承接
 * @param address 目的地
 * @param date 委托建立时间，格式为{yyyy-MM-dd HH:mm:ss}
 * @param remarks 委托备注
 */
final case class Order(odrId: String, stuId: Long, vtId: Long, status: Int, address: String, date: String, remarks: String) extends Entity