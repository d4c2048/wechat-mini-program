package com.lee.dao

import cn.hutool.core.util.ObjectUtil
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
    val student = mongodbTemplate.findOne(query, stuClass, stuCltName)
    if (ObjectUtil.isNull(student)) {
      return Student(0, null, 0, null, 0, null)
    }
    student.status = StudentStatus.ONLINE.code
    student
  }

  def updateById(stuId: Long, props: Map[String, Any]): Boolean = {
    val query = Query.query(Criteria.where(stuIdColName).is(stuId))
    val update = new Update
    props
      .filter(en => stuFields.contains(en._1))
      .foreach(en => update.set(en._1, en._2))
    mongodbTemplate
      .updateFirst(query, update, stuClass, stuCltName)
      .getModifiedCount > 1
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

  def addStudent(stu: Student): Boolean = {
    if (ObjectUtil.isNotNull(getStudentById(stu.stuId))) return false
    mongodbTemplate.insert(stu, stuCltName)
    mongodbTemplate.insert()
    ObjectUtil.isNotNull(getStudentById(stu.stuId))
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
