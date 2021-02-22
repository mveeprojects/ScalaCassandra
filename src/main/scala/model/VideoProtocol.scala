package model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{deserializationError, DefaultJsonProtocol, JsString, JsValue, RootJsonFormat}

import java.time.Instant
import java.util.UUID

object VideoProtocol extends SprayJsonSupport with DefaultJsonProtocol {

  implicit object UuidJsonFormat extends RootJsonFormat[UUID] {
    def write(uuid: UUID): JsString = JsString(uuid.toString)
    def read(value: JsValue): UUID = value match {
      case JsString(u) => UUID.fromString(u)
      case x           => deserializationError("Expected UUID as JsString, but got " + x)
    }
  }

  implicit object InstantJsonFormat extends RootJsonFormat[Instant] {
    def write(instant: Instant): JsString = JsString(instant.toString)
    def read(value: JsValue): Instant = value match {
      case JsString(i) => Instant.parse(i)
      case x           => deserializationError("Expected Instant as JsString, but got " + x)
    }
  }

  implicit val videoFormat: RootJsonFormat[Video] = jsonFormat4(Video)
}
