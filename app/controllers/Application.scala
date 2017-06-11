package controllers
import models.Student
import play.api.libs.json.Json
import play.api.mvc._
import services.StudentService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Application(studentService: StudentService, serverHost: String) extends Controller {

  def index = Action {
    Ok(views.html.index("Hai", None))
  }

  def login = Action.async {
    Future(Ok(views.html.index("Hai", Some("User"))))
  }

  def students() = Action.async {
    studentService.findStudents().map(students => {
      Ok(views.html.students(None, students))
    })
  }

  def student(id: String) = Action.async {
    studentService.studentDetails(id).map(student => {
      Ok(views.html.student(None, id, student))
    })
  }

  def search(q: String = "") = Action.async {
    studentService.searchStudents(q).map(students => {
      Ok(Json.toJson(students).toString)
    })
  }

  def updateStudent(id: Option[String]) = Action.async(parse.json) { implicit request =>
    if(id.isDefined){
      Future(Ok("Not Avalaible Now..!"))
    }else{
      request.body.validate[Student].fold(
        invalidJson => {
          Future(BadRequest("Not A Valid Student"))
        },
        validJson =>
          studentService.insertStudent(validJson).map(student => {
            Ok("Inserted")
          })
      )
    }
  }

}