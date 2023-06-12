package model

import java.time.Instant

case class Video(
    userId: String,
    videoId: String,
    title: Option[String],
    creationDate: Instant
)
