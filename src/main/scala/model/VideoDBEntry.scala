package model

import java.time.Instant

case class VideoDBEntry(userId: String, videoId: String, title: String, creationDate: Instant)
