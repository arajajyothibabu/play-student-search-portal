package models

import play.api.libs.json.Json

/**
  * Created by jyothi on 4/6/17.
  */
case class Student(
                    id: String, name: String, father: Option[String], mother: Option[String],
                    dob: Option[String], village: Option[String], occupation: Option[String], doj: Option[String],
                    religion: Option[String], caste: Option[String], admission: Option[Int], medium: Option[String]
                  )

object Student {
  implicit val studentFormat = Json.format[Student]
}
