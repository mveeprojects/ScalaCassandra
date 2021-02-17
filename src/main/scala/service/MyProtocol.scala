package service

import model.Video
import spray.json.{DefaultJsonProtocol, JsString, JsValue, RootJsonFormat, deserializationError}

import java.time.Instant
import java.util.UUID

object MyProtocol extends DefaultJsonProtocol {

  implicit object UuidJsonFormat extends RootJsonFormat[UUID] {
    def write(x: UUID): JsString = JsString(x.toString)
    def read(value: JsValue): UUID = value match {
      case JsString(x) => UUID.fromString(x)
      case x           => deserializationError("Expected UUID as JsString, but got " + x)
    }
  }

  implicit object InstantJsonFormat extends RootJsonFormat[Instant] {
    def write(i: Instant): JsString = JsString(i.toString)
    def read(value: JsValue): Instant = value match {
      case JsString(i) => Instant.parse(i)
      case x           => deserializationError("Expected Instant as JsString, but got " + x)
    }
  }

  implicit val videoFormat: RootJsonFormat[Video] = jsonFormat3(Video)
}
