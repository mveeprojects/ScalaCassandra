import utils.Logging

import java.time.Instant
import java.time.temporal.ChronoUnit

object Main extends App with Logging {

  private val KEYSPACE = "testKeyspace"

  val connector = new CassandraConnector
  connector.connect("cassandra", 9042, "datacenter1")

  val session = connector.session

  val keyspaceRepository = new KeyspaceRepository(session)
  keyspaceRepository.createKeyspace(KEYSPACE, 1)
  keyspaceRepository.useKeyspace(KEYSPACE)

  val videoRepository = new VideoRepository(session)
  videoRepository.createTable
  videoRepository.insertVideo(Video(None, "Video Title 1", Instant.now), Some(KEYSPACE))
  videoRepository.insertVideo(Video(None, "Video Title 2", Instant.now.minus(1, ChronoUnit.DAYS)), Some(KEYSPACE))

  val videos: Seq[Video] = videoRepository.selectAll(KEYSPACE)

  videos.foreach(v => logger.info(v.toString))

  connector.close(session)
}
