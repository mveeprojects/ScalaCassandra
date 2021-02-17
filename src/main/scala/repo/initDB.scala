package repo

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.`type`.DataTypes
import com.datastax.oss.driver.api.core.cql.{ResultSet, SimpleStatement}
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import config.AppConfig.appConfig.cassandra._
import utils.Logging

object initDB extends Logging {

  lazy val connector: CassandraConnector          = new CassandraConnector
  lazy val session: CqlSession                    = connector.setupSession(node, port, datacentre)
  lazy val keyspaceRepository: KeyspaceRepository = new KeyspaceRepository(session)
  lazy val videoRepository: VideoRepository       = new VideoRepository()

  def init: ResultSet = {
    lingerSeconds match {
      case Some(s) =>
        logger.info(s"Waiting $s seconds for Cassandra to get itself together.")
        Thread.sleep(s * 1000)
        logger.info("Ready or not, here I come.")
      case None => logger.info("Ready or not, here I come.")
    }
    keyspaceRepository.createKeyspaceIfNotExists(replicas)
    keyspaceRepository.useKeyspace(keyspace)
    createTableIfNotExists
  }

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
