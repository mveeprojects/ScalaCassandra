package repo

import config.AppConfig._
import model.Video

import scala.concurrent.Future

class VideoRepository {
  import db._

  def selectAllForUser(userId: String): Future[List[Video]] =
    db.run(quote {
      query[Video].filter(v => v.userId == lift(userId))
    })

  def selectFirstNForUser(userId: String, numberOfRecords: Int): Future[List[Video]] =
    selectAllForUser(userId).collect { case videos: List[Video] =>
      videos.take(numberOfRecords)
    }

  def insertVideoForUser(video: Video): Future[Unit] =
    db.run(quote {
      query[Video].insert(lift(video))
    })

  def deleteVideoForUser(userId: String, videoId: String): Future[Unit] =
    db.run(quote {
      query[Video]
        .filter(_.userId.equals(lift(userId)))
        .filter(_.videoId.equals(lift(videoId)))
        .delete
    })
}
