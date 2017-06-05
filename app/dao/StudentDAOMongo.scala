package dao

import models.Student
import play.api.libs.json.{JsObject, Json}
import reactivemongo.api.MongoConnection
import reactivemongo.play.json.collection.JSONCollection
import play.modules.reactivemongo.json._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by jyothi on 4/6/17.
  */
class StudentDAOMongo(mongoConnection: MongoConnection) {

  val collectionName = "students"
  val dbName = "nps"

  val npsDB = mongoConnection.apply(dbName)

  val studentCollection = npsDB[JSONCollection](collectionName)

  def insert(jsObject: JsObject): Future[Boolean] = {

    studentCollection.insert(jsObject).map { error =>
      error.ok match {
        case true => true //inserted
        case _ => false //insert failed
      }
    }

  }

  def read(query: Map[String, AnyRef], limit: Option[Int]): Future[List[Student]] = {

    val queryObject = Json.obj()

    val noOfDocumentToLimit = limit.fold(Integer.MAX_VALUE)(limit => limit)
    studentCollection.find(queryObject).cursor[Student]().collect[List](noOfDocumentToLimit)

  }

  def read(studentId: String): Future[Option[Student]] = {

    val queryObject = Json.obj("id" -> studentId)

    studentCollection.find(queryObject).one[Student]

  }

  def search(query: String, limit: Int = 10): Future[List[Student]] = {

    val queryObject = Json.obj(
      "name" -> Json.obj(
        "$regex" -> query
      )
    )
    studentCollection.find(queryObject).cursor[Student]().collect[List](limit)

  }

}