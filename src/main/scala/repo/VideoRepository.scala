package repo

import config.AppConfig._
import config.DBConfig.session
import model.Video

import java.time.Instant
import scala.concurrent.Future
import scala.jdk.CollectionConverters._

class VideoRepository {

  def selectAllForUser(userId: String): Future[List[Video]] = {
    val preparedStatement = session
      .prepare(s"SELECT * FROM ${appConfig.cassandra.keyspace}.video where userid = ?")
      .bind(userId)

    Future {
      session.execute(preparedStatement)
        .asScala
        .map(row => Video(
          row.getString("userid"),
          row.getString("videoid"),
          row.getString("title"),
          Instant.from(row.getLocalDate("creationdate"))
        )).toList
    }
  }

  def selectFirstNForUser(userId: String, numberOfRecords: Int): Future[List[Video]] =
    selectAllForUser(userId).collect { case videos: List[Video] =>
      videos.take(numberOfRecords)
    }

  def insertVideoForUser(video: Video): Future[Unit] = {
    val preparedStatement = session
      .prepare(s"INSERT INTO ${appConfig.cassandra.keyspace}.video (userid, videoid, title, creationdate) VALUES (?, ?, ?, ?)")
      .bind(video.userId, video.videoId, video.title, video.creationDate)

    Future {
      session.execute(preparedStatement)
    }
  }

  def deleteVideoForUser(userId: String, videoId: String): Future[Unit] = {
    val preparedStatement = session
      .prepare(s"DELETE FROM ${appConfig.cassandra.keyspace}.video WHERE userid = ? AND videoid = ?")
      .bind(userId, videoId)

    Future {
      session.execute(preparedStatement)
    }
  }
}
