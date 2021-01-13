import config.Configuration.appConfig.cassandra._
import model.Video
import repo.{CassandraConnector, KeyspaceRepository, VideoRepository}
import utils.Logging

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

object Main extends App with Logging {

  lingerSeconds match {
    case Some(s) =>
      logger.info(s"Waiting $s seconds for Cassandra to get itself together.")
      Thread.sleep(s * 1000)
      logger.info("Ready or not, here I come.")
    case None => logger.info("Ready or not, here I come.")
  }

  val connector          = new CassandraConnector
  val session            = connector.setupSession(node, port, datacentre)
  val keyspaceRepository = new KeyspaceRepository(session)
  val videoRepository    = new VideoRepository(session)

  keyspaceRepository.createKeyspaceIfNotExists(replicas)
  keyspaceRepository.useKeyspace(keyspace)

  videoRepository.createTableIfNotExists(tablename)

  videoRepository.insertVideo(
    Video(
      UUID.fromString("12345678-1234-1234-1234-123456789012"),
      "Video Title 1",
      Instant.now
    ),
    tablename
  )

  videoRepository.insertVideo(
    Video(
      UUID.fromString("12345678-1234-1234-1234-123456789013"),
      "Video Title 2",
      Instant.now.minus(1, ChronoUnit.DAYS)
    ),
    tablename
  )

  logger.info("Select all:")

  videoRepository
    .selectAll(tablename)
    .foreach(v => logger.info(v.toString))

  val selectOneResult = videoRepository
    .selectOne(tablename, UUID.fromString("12345678-1234-1234-1234-123456789012"))

  logger.info("Select one:")

  logger.info(selectOneResult.toString)

  connector.close(session)
}
