package models

import play.api.libs.json.Json

/**
  * Created by jyothi on 4/6/17.
  */
case class Student(
                    id: String, name: String, father: String, mother: String,
                    dob: String, village: String, occupation: String, doj: String,
                    religion: String, caste: String, admission: Int
                  )

object Student {
  implicit val studentFormat = Json.format[Student]
}
