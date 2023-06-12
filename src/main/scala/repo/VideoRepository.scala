package repo

import _root_.config.AppConfig
import _root_.config.DBConfig.session
import model.Video

import scala.concurrent.Future
import scala.jdk.CollectionConverters._
import scala.util.Try

class VideoRepository extends AppConfig {

  def selectAllForUser(userId: String): Future[List[Video]] = {
    val preparedStatement = session
      .prepare(s"SELECT * FROM ${cassandraConfig.cassandra.keyspace}.video where userid = ?")
      .bind(userId)

    Future {
      session.execute(preparedStatement)
        .asScala
        .map(row => Video(
          row.getString("userid"),
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
      .prepare(s"INSERT INTO ${cassandraConfig.cassandra.keyspace}.video (userid, videoid, title, creationdate) VALUES (?, ?, ?, ?)")
      .bind(userId, videoId, title.getOrElse("No title provided"), creationDate)

    Future {
      session.execute(preparedStatement)
    }
  }

  def deleteVideoForUser(userId: String, videoId: String): Future[Unit] = {
    val preparedStatement = session
      .prepare(s"DELETE FROM ${cassandraConfig.cassandra.keyspace}.video WHERE userid = ? AND videoid = ?")
      .bind(userId, videoId)

    Future {
      session.execute(preparedStatement)
    }
  }
}
