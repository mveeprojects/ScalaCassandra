package repo

import com.datastax.oss.driver.api.core.`type`.DataTypes
import com.datastax.oss.driver.api.core.cql.{ResultSet, SimpleStatement}
import com.datastax.oss.driver.api.core.{CqlIdentifier, CqlSession}
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace
import config.AppConfig._
import config.AppConfig.appConfig.cassandra._
import utils.Logging

object initDB extends Logging {

  lazy val session: CqlSession              = setupSession(node, port, datacentre)
  lazy val videoRepository: VideoRepository = new VideoRepository()

  def init: ResultSet = {
    lingerSeconds match {
      case Some(s) =>
        logger.info(s"Waiting $s seconds for Cassandra to get itself together.")
        Thread.sleep(s * 1000)
        logger.info("Ready or not, here I come.")
      case None => logger.info("Ready or not, here I come.")
    }
    createKeyspaceIfNotExists()
    useKeyspace()
    createTableIfNotExists
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
