package com.lee.dao

import com.lee.model.{LoginStudent, Role, Student, StudentStatus}
import org.springframework.data.mongodb.core.query.{Criteria, Query, Update}

import scala.jdk.CollectionConverters._

class StudentDao {
  import StudentDao._

  def getAllStudent: List[Student] = {
    mongodbTemplate
      .findAll(stuClass, stuCltName)
      .asScala
      .toList
  }

  def getStudentById(stuId: Long): Student = {
    val query = Query.query(Criteria.where(stuIdColName).is(stuId))
    mongodbTemplate.findOne(query, stuClass, stuCltName)
  }

  def authStudent(stuInfo: LoginStudent): Student = {
    val query = Query.query(Criteria.where(stuIdColName).is(stuInfo.stuId).and(pwdColName).is(stuInfo.pwd))
    mongodbTemplate.findOne(query, stuClass, stuCltName)
  }

  def updateById(stuId: Long, props: Map[String, Any]): Boolean = {
    val query = Query.query(Criteria.where(stuIdColName).is(stuId))
    var res = true
    props
      .filter(en => stuFields.contains(en._1))
      .foreach(en => {
        val (key, value) = en
        val update = Update.update(key, value)
        val updateResult = mongodbTemplate.updateFirst(query, update, stuClass, stuCltName)
        res = updateResult.getModifiedCount > 1 && res
      })
    res
  }

  def updateStatusById(stuId: Long, status: StudentStatus): Boolean = {
    updateById(stuId, Map(statusColName -> status.code))
  }

  def updateRoleById(stuId: Long, role: Role): Boolean = {
    updateById(stuId, Map(roleColName -> role.code))
  }

  def updatePwdById(stuId: Long, newPwd: String): Boolean = {
    updateById(stuId, Map(pwdColName -> newPwd))
  }
}

object StudentDao {
  private val stuClass = classOf[Student]
  private val stuCltName = "student"
  private val stuIdColName = "stuId"
  private val pwdColName = "pwd"
  private val statusColName = "status"
  private val roleColName = "role"
  private lazy val stuFields = classOf[Student].getDeclaredFields.map(_.getName)

  def apply(): StudentDao = new StudentDao
}
