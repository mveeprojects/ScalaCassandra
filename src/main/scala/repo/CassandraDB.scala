package repo

import com.datastax.oss.driver.api.core.CqlIdentifier
import com.datastax.oss.driver.api.core.`type`.DataTypes
import com.datastax.oss.driver.api.core.cql.{ResultSet, SimpleStatement}
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace
import config.AppConfig.appConfig.cassandra._
import config.CustomMetrics.cassandraReachable
import config.DBConfig.{closeSession, session}
import utils.Logging

import scala.util.{Failure, Success, Try}

object CassandraDB extends Logging {

  def init(): Unit = {
    logger.info("Configuring Keyspace and Schema in Cassandra...")
    Try {
      createKeyspaceIfNotExists()
      useKeyspace()
      createTableIfNotExists
    } match {
      case Success(_) =>
        logger.info("DB initialisation completed successfully.")
        cassandraReachable.update(1)
        closeSession(session)
      case Failure(exception) =>
        logger.error(s"Exception thrown during DB initialisation => ${exception.getMessage}")
        closeSession(session)
    }
  }

  private def createKeyspaceIfNotExists(): Unit = {
    val cks: CreateKeyspace = SchemaBuilder
      .createKeyspace(keyspace)
      .ifNotExists
      .withSimpleStrategy(replicas)
    session.execute(cks.build)
  }

  private def useKeyspace(): ResultSet =
    session.execute("USE " + CqlIdentifier.fromCql(keyspace))

  private def createTableIfNotExists: ResultSet = {
    val statement: SimpleStatement = SchemaBuilder
      .createTable(tablename)
      .ifNotExists
      .withPartitionKey("userid", DataTypes.TEXT)
      .withClusteringColumn("videoid", DataTypes.TEXT)
      .withColumn("title", DataTypes.TEXT)
      .withColumn("creationdate", DataTypes.TIMESTAMP)
      .build
    session.execute(statement)
  }
}
