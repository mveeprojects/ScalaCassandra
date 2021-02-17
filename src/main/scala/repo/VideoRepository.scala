package repo

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.`type`.DataTypes
import com.datastax.oss.driver.api.core.cql._
import com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal
import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert
import com.datastax.oss.driver.api.querybuilder.{QueryBuilder, SchemaBuilder}
import config.AppConfig.appConfig.cassandra._
import model.VideoDBEntry
import utils.Logging

import scala.jdk.CollectionConverters.IterableHasAsScala

class VideoRepository(session: CqlSession) extends Logging {

  def createTableIfNotExists: ResultSet = {
    val statement: SimpleStatement = SchemaBuilder
      .createTable(tablename)
      .ifNotExists
      .withPartitionKey("user_id", DataTypes.TEXT)
      .withColumn("video_id", DataTypes.TEXT)
      .withColumn("title", DataTypes.TEXT)
      .withColumn("creation_date", DataTypes.TIMESTAMP)
      .build
    executeStatement(statement)
  }

  def insertVideo(video: VideoDBEntry): ResultSet = {
    val insertInto: RegularInsert = QueryBuilder
      .insertInto(tablename)
      .value("user_id", QueryBuilder.bindMarker())
      .value("video_id", QueryBuilder.bindMarker())
      .value("title", QueryBuilder.bindMarker())
      .value("creation_date", QueryBuilder.bindMarker())
    val preparedStatement: PreparedStatement = session.prepare(insertInto.build)
    val statement: BoundStatement = preparedStatement
      .bind()
      .setString(0, video.userId)
      .setString(1, video.videoId)
      .setString(2, video.title)
      .setInstant(3, video.creationDate)
    executeStatement(statement)
  }

  def deleteVideo(userId: String, videoId: String): Unit = {
    val deleteFrom = QueryBuilder
      .deleteFrom(tablename)
      .whereColumn("user_id").isEqualTo(literal(userId))
      .ifColumn("video_id").isEqualTo(literal(videoId))
      .build()
    executeStatement(deleteFrom)
  }

  def selectAllForUser(userId: String): List[VideoDBEntry] = {
    val statement: SimpleStatement = QueryBuilder.selectFrom(tablename).all.build
    val resultSet: ResultSet       = executeStatement(statement)
    deSerialiseSelect(resultSet).filter(_.userId.equals(userId))
  }

  def selectFirstNForUser(userId: String, numberOfRecords: Int): List[VideoDBEntry] =
    selectAllForUser(userId)
      .take(numberOfRecords)

  def deSerialiseSelect(resultSet: ResultSet): List[VideoDBEntry] = resultSet
    .map(v =>
      VideoDBEntry(
        v.getString("user_id"),
        v.getString("video_id"),
        v.getString("title"),
        v.getInstant("creation_date")
      )
    )
    .asScala
    .toList

  def executeStatement[T <: BatchableStatement[T]](statement: BatchableStatement[T]): ResultSet =
    session.execute(statement)
}
