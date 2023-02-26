package com.lee.dao

import cn.hutool.core.util.ObjectUtil
import com.lee.model.{Order, OrderStatus}
import org.springframework.data.mongodb.core.query.{Criteria, Query, Update, UpdateDefinition}

import scala.jdk.CollectionConverters._

class OrderDao {
  import OrderDao._

  def getAllOrder: List[Order] = {
    mongodbTemplate.findAll(odrClass, odrCltName)
      .asScala
      .toList
  }

  def getOrderById(odrId: String): Order = {
    val query = Query.query(Criteria.where(odrIdColName).is(odrId))
    mongodbTemplate.findOne(query, odrClass, odrCltName)
  }

  def addOrder(order: Order): Boolean = {
    val res = mongodbTemplate.insert(order, odrCltName)
    ObjectUtil.isNotNull(res)
  }

  def updateById(odrId: String, props: Map[String, Any]): Boolean = {
    val query = Query.query(Criteria.where(odrIdColName).is(odrId))
    var res = true
    props
      .filter(en => odrFields.contains(en._1))
      .foreach(en => {
        val (key, value) = en
        val update = Update.update(key, value)
        val updateResult = mongodbTemplate.updateFirst(query, update, odrClass, odrCltName)
        res = res && updateResult.getModifiedCount > 0
      })
    res
  }

  def updateOrderStatusById(odrId: String, status: OrderStatus): Boolean = {
    updateById(odrId, Map(statusColName -> status.code))
  }
}

object OrderDao {
  private val odrClass = classOf[Order]
  private val odrCltName = "order"
  private val odrIdColName = "odrId"
  private val statusColName = "status"
  private lazy val odrFields = odrClass.getDeclaredFields.map(_.getName)

  def apply(): OrderDao = new OrderDao
}
