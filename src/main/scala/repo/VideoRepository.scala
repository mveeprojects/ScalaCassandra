package repo

import com.datastax.oss.driver.api.core.`type`.DataTypes
import com.datastax.oss.driver.api.core.cql.{BoundStatement, PreparedStatement, ResultSet, SimpleStatement}
import com.datastax.oss.driver.api.core.{CqlIdentifier, CqlSession}
import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert
import com.datastax.oss.driver.api.querybuilder.select.Select
import com.datastax.oss.driver.api.querybuilder.{QueryBuilder, SchemaBuilder}
import model.Video
import utils.Logging

import java.util.UUID
import scala.jdk.CollectionConverters.IterableHasAsScala

class VideoRepository(session: CqlSession) extends Logging {

  def createTableIfNotExists(keyspace: String, tableName: String): ResultSet = {
    val ct: SimpleStatement = SchemaBuilder
      .createTable(tableName)
      .ifNotExists
      .withPartitionKey("video_id", DataTypes.UUID)
      .withColumn("title", DataTypes.TEXT)
      .withColumn("creation_date", DataTypes.TIMESTAMP)
      .build

    executeStatement(ct, Some(keyspace))
  }

  def insertVideo(video: Video, tableName: String): UUID = {
    val vid: Video = video.copy(id = Some(UUID.randomUUID))

    val insertInto: RegularInsert = QueryBuilder
      .insertInto(tableName)
      .value("video_id", QueryBuilder.bindMarker())
      .value("title", QueryBuilder.bindMarker())
      .value("creation_date", QueryBuilder.bindMarker())

    val preparedStatement: PreparedStatement = session.prepare(insertInto.build)

    val statement: BoundStatement = preparedStatement
      .bind()
      .setUuid(0, vid.id.get)
      .setString(1, vid.title)
      .setInstant(2, vid.creationDate)

    session.execute(statement)
    vid.id.get
  }

  def selectAll(keyspace: String, tableName: String): List[Video] = {
    val select: Select = QueryBuilder.selectFrom(tableName).all
    executeStatement(select.build, Some(keyspace))
      .map(v =>
        Video(
          Some(v.getUuid("video_id")),
          v.getString("title"),
          v.getInstant("creation_date")
        )
      )
      .asScala
      .toList
  }

  def executeStatement(statement: SimpleStatement, maybeKeyspace: Option[String]): ResultSet =
    maybeKeyspace match {
      case Some(keyspace) =>
        logger.info("With keyspace")
        statement.setKeyspace(CqlIdentifier.fromCql(keyspace))
        session.execute(statement)
      case None =>
        logger.info("Without keyspace")
        session.execute(statement)
    }
}
