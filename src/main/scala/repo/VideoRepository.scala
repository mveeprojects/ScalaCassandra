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
import scala.jdk.CollectionConverters.IterableHasAsScala

class VideoRepository(session: CqlSession) extends Logging {

  def createTableIfNotExists: ResultSet = {
    val statement: SimpleStatement = SchemaBuilder
      .createTable(tablename)
      .ifNotExists
      .withPartitionKey("userId", DataTypes.TEXT)
      .withColumn("videoId", DataTypes.TEXT)
      .withColumn("title", DataTypes.TEXT)
      .withColumn("creationDate", DataTypes.TIMESTAMP)
      .build
    executeStatement(statement)
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
    executeStatement(statement)
  }

  def deleteVideo(userId: String, videoId: String): Unit = {
    val deleteFrom = QueryBuilder
      .deleteFrom(tablename)
      .whereColumn("userId")
      .isEqualTo(literal(userId))
      .ifColumn("videoId")
      .isEqualTo(literal(videoId))
      .build()
    executeStatement(deleteFrom)
  }

  def selectAllForUser(userId: String): List[Video] = {
    val statement: SimpleStatement = QueryBuilder.selectFrom(tablename).all.build
    val resultSet: ResultSet       = executeStatement(statement)
    deSerialiseSelect(resultSet).filter(_.userId.equals(userId))
  }

  private val cluster = Cluster
    .builder()
    .addContactPoint("cassandra")
    .withoutJMXReporting
    .build()
  private val db = new CassandraAsyncContext(CamelCase, cluster, keyspace, 100)
  import db._

  def selectAllForUserQuill(userId: String): Future[List[Video]] =
    db.run(quote {
      query[Video].filter(v => v.userId == lift(userId))
    })

  def selectFirstNForUser(userId: String, numberOfRecords: Int): List[Video] =
    selectAllForUser(userId)
      .take(numberOfRecords)

  def deSerialiseSelect(resultSet: ResultSet): List[Video] = resultSet
    .map(v =>
      Video(
        v.getString("userId"),
        v.getString("videoId"),
        v.getString("title"),
        v.getInstant("creationDate")
      )
    )
    .asScala
    .toList

  def executeStatement[T <: BatchableStatement[T]](statement: BatchableStatement[T]): ResultSet =
    session.execute(statement)
}
