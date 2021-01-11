package model

import java.time.Instant
import java.util.UUID

case class Video(id: Option[UUID], title: String, creationDate: Instant) {
  override def toString: String = s"$id - $title - $creationDate"
}
