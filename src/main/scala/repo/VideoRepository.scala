package repo

import com.datastax.driver.core.Cluster
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.`type`.DataTypes
import com.datastax.oss.driver.api.core.cql._
import com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal
import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert
import com.datastax.oss.driver.api.querybuilder.{QueryBuilder, SchemaBuilder}
import config.AppConfig.appConfig.cassandra._
import io.getquill.{CamelCase, CassandraAsyncContext}
import model.Video
import utils.Logging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class VideoRepository(session: CqlSession) extends Logging {

  def createTableIfNotExists: ResultSet = {
    val statement: SimpleStatement = SchemaBuilder
      .createTable(tablename)
      .ifNotExists
      .withPartitionKey("userId", DataTypes.TEXT)
      .withClusteringColumn("videoId", DataTypes.TEXT)
      .withColumn("title", DataTypes.TEXT)
      .withColumn("creationDate", DataTypes.TIMESTAMP)
      .build
    session.execute(statement)
  }

  def insertVideo(video: Video): ResultSet = {
    val insertInto: RegularInsert = QueryBuilder
      .insertInto(tablename)
      .value("userId", QueryBuilder.bindMarker())
      .value("videoId", QueryBuilder.bindMarker())
      .value("title", QueryBuilder.bindMarker())
      .value("creationDate", QueryBuilder.bindMarker())
    val preparedStatement: PreparedStatement = session.prepare(insertInto.build)
    val statement: BoundStatement = preparedStatement
      .bind()
      .setString(0, video.userId)
      .setString(1, video.videoId)
      .setString(2, video.title)
      .setInstant(3, video.creationDate)
    session.execute(statement)
  }

  def deleteVideo(userId: String, videoId: String): Unit = {
    val deleteFrom = QueryBuilder
      .deleteFrom(tablename)
      .whereColumn("userId")
      .isEqualTo(literal(userId))
      .ifColumn("videoId")
      .isEqualTo(literal(videoId))
      .build()
    session.execute(deleteFrom)
  }

  private val cluster = Cluster
    .builder()
    .addContactPoint("cassandra")
    .withoutJMXReporting
    .build()
  private val db = new CassandraAsyncContext(CamelCase, cluster, keyspace, 100)
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
}
