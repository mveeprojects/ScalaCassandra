package repo

import com.datastax.oss.driver.api.core.`type`.DataTypes
import com.datastax.oss.driver.api.core.cql.{ResultSet, SimpleStatement}
import com.datastax.oss.driver.api.core.{CqlIdentifier, CqlSession}
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace
import config.AppConfig.appConfig.cassandra._
import config.DBConfig.{closeDBInitSession, openDBInitSession}
import utils.Logging

import scala.util.{Failure, Success, Try}

object CassandraDB extends Logging {

  lazy val session: CqlSession         = openDBInitSession(host, port, datacentre)
  val videoRepository: VideoRepository = new VideoRepository()

  def init(): Unit = {
    logger.info("Configuring Keyspace and Schema in Cassandra...")
    Try {
      createKeyspaceIfNotExists()
      useKeyspace()
      createTableIfNotExists
    } match {
      case Success(_) =>
        logger.info("DB initialisation completed successfully.")
        closeDBInitSession(session)
      case Failure(exception) =>
        logger.error(s"Exception thrown during DB initialisation => ${exception.getMessage}")
        closeDBInitSession(session)
    }
  }

  def createKeyspaceIfNotExists(): Unit = {
    val cks: CreateKeyspace = SchemaBuilder
      .createKeyspace(keyspace)
      .ifNotExists
      .withSimpleStrategy(replicas)
    session.execute(cks.build)
  }

  def useKeyspace(): ResultSet =
    session.execute("USE " + CqlIdentifier.fromCql(keyspace))

  private def createTableIfNotExists: ResultSet = {
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
}
