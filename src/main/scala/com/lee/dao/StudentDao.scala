package com.lee.dao

import com.lee.model.{LoginStudent, Student, StudentStatus}
import org.springframework.data.mongodb.core.query.{Criteria, Query, Update}

import scala.jdk.CollectionConverters._

class StudentDao {
  import StudentDao._

  def getAllStudent: List[Student] = {
    mongodbTemplate
      .findAll(stuClass, stuCollectionName)
      .asScala
      .toList
  }

  def getStudentById(stuId: Long): Student = {
    val query = Query.query(Criteria.where(stuIdColName).is(stuId))
    mongodbTemplate.findOne(query, stuClass, stuCollectionName)
  }

  def authStudent(stuInfo: LoginStudent): Student = {
    val query = Query.query(Criteria.where(stuIdColName).is(stuInfo.stuId).and(pwdColName).is(stuInfo.pwd))
    mongodbTemplate.findOne(query, stuClass, stuCollectionName)
  }

  def updateStudentStatusById(stuId: Long, status: StudentStatus): Boolean = {
    val query = Query.query(Criteria.where(stuIdColName).is(stuId))
    val update = Update.update(statusColName, status.code)
    val result = mongodbTemplate.updateFirst(query, update, stuClass, stuCollectionName)
    result.getModifiedCount > 0
  }
}

object StudentDao {
  private val stuClass = classOf[Student]
  private val stuCollectionName = "student"
  private val stuIdColName = "stuId"
  private val pwdColName = "pwd"
  private val statusColName = "status"
  private val roleColName = "role"

  def apply(): StudentDao = new StudentDao
}
