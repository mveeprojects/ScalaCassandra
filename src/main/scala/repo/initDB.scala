package repo

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.ResultSet
import config.AppConfig.appConfig.cassandra._
import model.VideoDBEntry
import utils.Logging

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

object initDB extends Logging {

  lazy val connector: CassandraConnector          = new CassandraConnector
  lazy val session: CqlSession                    = connector.setupSession(node, port, datacentre)
  lazy val keyspaceRepository: KeyspaceRepository = new KeyspaceRepository(session)
  lazy val videoRepository: VideoRepository       = new VideoRepository(session)

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
    videoRepository.createTableIfNotExists
  }

//  def addSampleData(): Unit = {
//    videoRepository.insertVideo(
//      VideoDBEntry(
//        "1234",
//        UUID.fromString("12345678-1234-1234-1234-123456789012"),
//        "Video Title 1",
//        Instant.now
//      )
//    )
//    videoRepository.insertVideo(
//      VideoDBEntry(
//        "5678",
//        UUID.fromString("12345678-1234-1234-1234-123456789013"),
//        "Video Title 2",
//        Instant.now.minus(1, ChronoUnit.DAYS)
//      )
//    )
//  }
}
