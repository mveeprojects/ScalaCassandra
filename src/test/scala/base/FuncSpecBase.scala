package base

import akka.http.scaladsl.model.StatusCodes
import org.scalatest.concurrent.Eventually
import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import utils.DBUtils.{closeTestCassandraSession, removeVideoFromDB}
import utils.HttpUtils.fireReadinessRequest
import utils.Logging

trait FuncSpecBase
    extends AnyFreeSpec
    with Matchers
    with Eventually
    with BeforeAndAfterEach
    with BeforeAndAfterAll
    with Logging {

  val testUserId  = "userA"
  val testVideoId = "videoA"

  override def beforeAll(): Unit = {
    logger.info("Waiting for app to become ready.")
    eventually {
      val testAppReadiness = fireReadinessRequest()
      testAppReadiness.futureValue shouldBe StatusCodes.Accepted
    }
    logger.info("App ready to receive requests.")
  }

  override def beforeEach(): Unit =
    removeVideoFromDB(testUserId, testVideoId)

  override def afterAll(): Unit =
    closeTestCassandraSession()
}
