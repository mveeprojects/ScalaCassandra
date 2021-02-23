package utils

import com.datastax.driver.core.Cluster
import io.getquill.{CamelCase, CassandraAsyncContext}
import model.Video

import java.time.Instant
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object DBUtils {

  private lazy val testQuillCluster = Cluster
    .builder()
    .addContactPoint("localhost")
    .withoutJMXReporting
    .build()

  private val testQuillDB = new CassandraAsyncContext(CamelCase, testQuillCluster, "testKeyspace", 100)

  import testQuillDB._

  def insertVideoIntoDB(userId: String, videoId: String): Future[Unit] =
    testQuillDB.run(quote {
      query[Video].insert(lift(Video(userId, videoId, "video title", Instant.now())))
    })

  def removeVideosFromDB(userId: String): Future[Unit] =
    testQuillDB.run(quote {
      query[Video]
        .filter(_.userId.equals(lift(userId)))
        .delete
    })

  def closeTestCassandraSession(): Unit = {
    testQuillDB.close()
  }
}
