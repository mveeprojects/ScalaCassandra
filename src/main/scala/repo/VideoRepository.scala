package repo

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.`type`.DataTypes
import com.datastax.oss.driver.api.core.cql._
import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert
import com.datastax.oss.driver.api.querybuilder.{QueryBuilder, SchemaBuilder}
import model.Video
import utils.Logging

import java.util.UUID
import scala.jdk.CollectionConverters.IterableHasAsScala

class VideoRepository(session: CqlSession) extends Logging {

  def createTableIfNotExists(tableName: String): ResultSet = {
    val statement: SimpleStatement = SchemaBuilder
      .createTable(tableName)
      .ifNotExists
      .withPartitionKey("video_id", DataTypes.UUID)
      .withColumn("title", DataTypes.TEXT)
      .withColumn("creation_date", DataTypes.TIMESTAMP)
      .build
    executeStatement(statement)
  }

  def insertVideo(video: Video, tableName: String): UUID = {
    val insertInto: RegularInsert = QueryBuilder
      .insertInto(tableName)
      .value("video_id", QueryBuilder.bindMarker())
      .value("title", QueryBuilder.bindMarker())
      .value("creation_date", QueryBuilder.bindMarker())
    val preparedStatement: PreparedStatement = session.prepare(insertInto.build)
    val statement: BoundStatement = preparedStatement
      .bind()
      .setUuid(0, video.id)
      .setString(1, video.title)
      .setInstant(2, video.creationDate)
    executeStatement(statement)
    video.id
  }

  def selectAll(tableName: String): List[Video] = {
    val statement: SimpleStatement = QueryBuilder.selectFrom(tableName).all.build
    val resultSet: ResultSet       = executeStatement(statement)
    deSerialiseSelect(resultSet)
  }

  def selectOne(tableName: String, video_id: UUID): Video = {
    val statement: SimpleStatement = QueryBuilder.selectFrom(tableName).all.build
    val resultSet: ResultSet       = executeStatement(statement)
    deSerialiseSelect(resultSet)
      .filter(v => v.id.equals(video_id))
      .head
  }

  def deSerialiseSelect(resultSet: ResultSet): List[Video] = resultSet
    .map(v =>
      Video(
        v.getUuid("video_id"),
        v.getString("title"),
        v.getInstant("creation_date")
      )
    )
    .asScala
    .toList

  def executeStatement[T <: BatchableStatement[T]](statement: BatchableStatement[T]): ResultSet =
    session.execute(statement)
}
