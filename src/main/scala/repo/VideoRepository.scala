package repo

import config.AppConfig._
import config.DBConfig.session
import model.Video

import java.util.UUID
import scala.concurrent.Future
import scala.jdk.CollectionConverters._
import scala.util.Try

class VideoRepository {

  def selectAllForUser(userId: String): Future[List[Video]] = {
    val preparedStatement = session
      .prepare(s"SELECT * FROM ${appConfig.cassandra.keyspace}.video where userid = ?")
      .bind(userId)

    Future {
      session.execute(preparedStatement)
        .asScala
        .map(row => Video(
          UUID.fromString(row.getString("userid")),
          row.getString("videoid"),
          Try(row.getString("title")).toOption,
          row.getInstant("creationdate")
        )).toList
    }
  }

  def selectFirstNForUser(userId: String, numberOfRecords: Int): Future[List[Video]] =
    selectAllForUser(userId).collect { case videos: List[Video] =>
      videos.take(numberOfRecords)
    }

  def insertVideoForUser(video: Video): Future[Unit] = {
    import video._
    val preparedStatement = session
      .prepare(s"INSERT INTO ${appConfig.cassandra.keyspace}.video (userid, videoid, title, creationdate) VALUES (?, ?, ?, ?)")
      .bind(userId.toString, videoId, title.getOrElse("No title provided"), creationDate)

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
