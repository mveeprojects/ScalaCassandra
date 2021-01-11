import config.Configuration.appConfig.cassandra._
import model.Video
import repo.{CassandraConnector, KeyspaceRepository, VideoRepository}
import utils.Logging

import java.time.Instant
import java.time.temporal.ChronoUnit

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

  keyspaceRepository.createKeyspaceIfNotExists(keyspace, replicas)
  keyspaceRepository.useKeyspace(keyspace)

  videoRepository.createTableIfNotExists(keyspace, tablename)
  videoRepository.insertVideo(Video(None, "Video Title 1", Instant.now), tablename)
  videoRepository.insertVideo(Video(None, "Video Title 2", Instant.now.minus(1, ChronoUnit.DAYS)), tablename)

  videoRepository
    .selectAll(keyspace, tablename)
    .foreach(v => logger.info(v.toString))

  connector.close(session)
}
