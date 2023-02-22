package utils

import com.datastax.oss.driver.api.core.CqlSession
import config.AppConfig.appConfig

import java.net.InetSocketAddress
import java.time.Instant
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object DBUtils {

  def testSession: CqlSession = {
    CqlSession.builder
      .addContactPoint(new InetSocketAddress("cassandra", 9042))
      .withLocalDatacenter("datacenter1")
      .build
  }

  def insertVideoIntoDB(userId: String, videoId: String): Future[Unit] = {
    val preparedStatement = testSession
      .prepare("INSERT INTO testKeyspace.video (userid, videoid, title, creationdate) VALUES (?, ?, ?, ?)")
      .bind(userId, videoId, "video title", Instant.now())

    Future {
      testSession.execute(preparedStatement)
    }
  }

  def removeVideosFromDB(userId: String): Future[Unit] = {
    val preparedStatement = testSession
      .prepare("DELETE FROM testKeyspace.video WHERE userid = ?")
      .bind(userId)

    Future {
      testSession.execute(preparedStatement)
    }
  }

  def closeTestCassandraSession(): Unit = {
    testSession.close()
  }
}
