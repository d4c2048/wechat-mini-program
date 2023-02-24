package com.lee.dao

import cn.hutool.core.util.ObjectUtil
import com.lee.model.{Order, OrderStatus}
import org.springframework.data.mongodb.core.query.{Criteria, Query, Update, UpdateDefinition}

import scala.jdk.CollectionConverters._

class OrderDao {
  import OrderDao._

  def getAllOrder: List[Order] = {
    mongodbTemplate.findAll(odrClass, odrCollectionName)
      .asScala
      .toList
  }

  def getOrderById(odrId: String): Order = {
    val query = Query.query(Criteria.where("odrId").is(odrId))
    mongodbTemplate.findOne(query, odrClass, odrCollectionName)
  }

  def addOrder(order: Order): Boolean = {
    val res = mongodbTemplate.insert(order, odrCollectionName)
    ObjectUtil.isNotNull(res)
  }

  def updateById(odrId: String, props: Map[String, Any]): Boolean = {
    val update = new Update
    //Update.update().addToSet()
    val legalProps = props.filter(entry => orderFields.contains(entry._1))
    false
  }

  def updateOrderStatusById(odrId: String, status: OrderStatus): Boolean = {
    val query = Query.query(Criteria.where("odrId").is(odrId))
    val update = Update.update("status", status.code)
    val result = mongodbTemplate.updateFirst(query, update, odrClass, odrCollectionName)
    result.getModifiedCount > 0
  }
}

object OrderDao {
  private val odrClass = classOf[Order]
  private val odrCollectionName = "order"
  private lazy val orderFields = odrClass.getDeclaredFields.map(_.getName)

  def apply(): OrderDao = new OrderDao
}
