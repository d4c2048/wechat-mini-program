package com.lee.dao

import com.lee.model.{LoginStudent, Student}
import org.springframework.data.mongodb.core.query.{Criteria, Query}

import scala.jdk.CollectionConverters._

class StudentDao {
  private val stuClass = classOf[Student]

  def getAllStudent: List[Student] = mongodbTemplate
    .findAll(stuClass, "student")
    .asScala
    .toList

  def getStudentById(stuId: Long): Student = {
    val query = Query.query(Criteria.where("stuId").is(stuId))
    mongodbTemplate.findOne(query, stuClass, "student")
  }

  def authStudent(stuInfo: LoginStudent): Student = {
    val query = Query.query(Criteria.where("stuId").is(stuInfo.stuId).and("pwd").is(stuInfo.pwd))
    mongodbTemplate.findOne(query, stuClass, "student")
  }
}

object StudentDao {
  def apply(): StudentDao = new StudentDao
}
