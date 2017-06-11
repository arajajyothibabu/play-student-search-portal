package services

import dao.StudentDAOMongo
import models.Student
import play.api.libs.json.{JsObject, Json}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by jyothi on 17/4/17.
  */
class StudentService(studentDAO: StudentDAOMongo) {


  def insertStudent(student: Student): Future[Boolean] = {
    studentDAO.insert(Json.toJson(student).as[JsObject])
  }

  def findStudents(medium: Option[String]): Future[List[Student]] = {
    val query = if(medium.isDefined){
      Map("medium" -> medium.get)
    }else{
      Map.empty[String, String]
    }
    studentDAO.read(query, None)
  }

  def studentDetails(id: String): Future[Option[Student]] = {
    studentDAO.read(id)
  }

  def searchStudents(query: String, limit: Int = 10): Future[List[Student]] = {
    studentDAO.search(query, limit)
  }


}
