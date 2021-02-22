package model

import java.time.Instant

case class Video(
    userId: String,
    videoId: String,
    title: String,
    creationDate: Instant
)
