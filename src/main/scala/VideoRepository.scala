import com.datastax.oss.driver.api.core.`type`.DataTypes
import com.datastax.oss.driver.api.core.cql.{BoundStatement, PreparedStatement, ResultSet, SimpleStatement}
import com.datastax.oss.driver.api.core.{CqlIdentifier, CqlSession}
import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert
import com.datastax.oss.driver.api.querybuilder.select.Select
import com.datastax.oss.driver.api.querybuilder.{QueryBuilder, SchemaBuilder}

import java.util.UUID
import scala.jdk.CollectionConverters.IterableHasAsScala

class VideoRepository(session: CqlSession) {
  private val TABLE_NAME: String = "videos"

  def createTable: ResultSet = createTable(None)

  def createTable(keyspace: Option[String]): ResultSet = {
    val ct: SimpleStatement = SchemaBuilder
      .createTable(TABLE_NAME)
      .withPartitionKey("video_id", DataTypes.UUID)
      .withColumn("title", DataTypes.TEXT)
      .withColumn("creation_date", DataTypes.TIMESTAMP)
      .build

    executeStatement(ct, keyspace)
  }

  def insertVideo(video: Video, maybeKeyspace: Option[String]): UUID = {
    val vid: Video = video.copy(id = Some(UUID.randomUUID))

    val insertInto: RegularInsert = QueryBuilder
      .insertInto(TABLE_NAME)
      .value("video_id", QueryBuilder.bindMarker())
      .value("title", QueryBuilder.bindMarker())
      .value("creation_date", QueryBuilder.bindMarker())

    val insertStatement: SimpleStatement = maybeKeyspace match {
      case Some(keyspace) => insertInto.build.setKeyspace(keyspace)
      case None           => insertInto.build
    }

    val preparedStatement: PreparedStatement = session.prepare(insertStatement)

    val statement: BoundStatement = preparedStatement
      .bind()
      .setUuid(0, vid.id.get)
      .setString(1, vid.title)
      .setInstant(2, vid.creationDate)

    session.execute(statement)
    vid.id.get
  }

  def selectAll(keyspace: String): List[Video] = {
    val select: Select       = QueryBuilder.selectFrom(TABLE_NAME).all
    val resultSet: ResultSet = executeStatement(select.build, Some(keyspace))
    resultSet
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
        statement.setKeyspace(CqlIdentifier.fromCql(keyspace))
        session.execute(statement)
      case None => session.execute(statement)
    }

}
