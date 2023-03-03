package com.lee.dao

import cn.hutool.core.util.ObjectUtil
import com.lee.model.{Order, OrderStatus, orderOrdering}
import org.springframework.data.mongodb.core.query._

import scala.jdk.CollectionConverters._

class OrderDao {
  import OrderDao._

  def getAllOrderByStuId(stuId: Long): List[Order] = {
    val query = Query.query(Criteria.where("stuId").is(stuId))
    mongodbTemplate
      .find(query, odrClass, odrCltName)
      .asScala
      .toList
      .sorted
  }

  def getAllNoClaimOrder: List[Order] = {
    val criteria = Criteria
      .where(statusColName)
      .gte(OrderStatus.NO_CLAIMED.code)
      .lte(OrderStatus.OVER_THREE_DAYS.code)
    val query = Query.query(criteria)
    mongodbTemplate
      .find(query, odrClass, odrCltName)
      .asScala
      .toList
      .sorted
  }

  def getOrderById(odrId: String): Order = {
    val query = Query.query(Criteria.where(odrIdColName).is(odrId))
    mongodbTemplate.findOne(query, odrClass, odrCltName)
  }

  def getAllCheckOrder: List[Order] = {
    val criteria = Array(
      Criteria.where("status").is(OrderStatus.NO_CLAIMED.code),
      Criteria.where("status").is(OrderStatus.OVER_ONE_DAY.code)
    )
    val query = Query.query(new Criteria().orOperator(criteria: _*))
    mongodbTemplate
      .find(query, odrClass, odrCltName)
      .asScala
      .toList
  }

  def addOrder(order: Order): Boolean = {
    val res = mongodbTemplate.insert(order, odrCltName)
    ObjectUtil.isNotNull(res)
  }

  def updateById(odrId: String, props: Map[String, Any]): Boolean = {
    val query = Query.query(Criteria.where(odrIdColName).is(odrId))
    val update = new Update
    props
      .filter(en => odrFields.contains(en._1))
      .foreach(en => update.set(en._1, en._2))
    mongodbTemplate
      .updateFirst(query, update, odrClass, odrCltName)
      .getModifiedCount > 1
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
