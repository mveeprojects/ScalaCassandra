package model

import java.time.Instant
import java.util.UUID

case class Video(
    userId: UUID,
    videoId: String,
    title: Option[String],
    creationDate: Instant
)
