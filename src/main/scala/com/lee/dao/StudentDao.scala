package com.lee.dao

import com.lee.model.{LoginStudent, Student}
import org.springframework.data.mongodb.core.query.{Criteria, Query}

import scala.jdk.CollectionConverters._

class StudentDao() {
  private val stuClass = classOf[Student]

  def getAllStudent: List[Student] = mongodbTemplate
    .findAll(stuClass, "student")
    .asScala
    .toList

  def getStudentById(stuId: Long): Student = {
    val query = Query.query(Criteria.where("stuId").is(stuId))
    mongodbTemplate.findOne(query, stuClass, "student")
  }

  def authStudent(userInfo: LoginStudent): Student = {
    val query = Query.query(Criteria.where("stuId").is(userInfo.stuId).and("pwd").is(userInfo.pwd))
    mongodbTemplate.findOne(query, stuClass, "student")
  }
}
