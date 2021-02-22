package route

import akka.http.scaladsl.model.StatusCodes
import config.TestConfig.testConf._
import model.Video
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.concurrent.Eventually
import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import utils.DBUtils._
import utils.HttpUtils._

import scala.concurrent.Future

class ApiRoutesSpec extends AnyFreeSpec with Matchers with Eventually with BeforeAndAfterEach with BeforeAndAfterAll {

  override implicit val patienceConfig: PatienceConfig =
    PatienceConfig(patience.timeout, patience.interval)

  private val testUserId  = "userA"
  private val testVideoId = "videoA"

  override def beforeAll(): Unit = Thread.sleep(45000)

  override def beforeEach(): Unit = removeVideoFromDB(testUserId, testVideoId)

  override def afterAll(): Unit = closeTestCassandraSession()

  "Api routes" - {
    "when GET is called for a userId that does not exist" - {
      "should return an empty list of videos" in {
        eventually {
          val getResult: Future[Seq[Video]] = fireGetRequest("unknownuser")
          getResult.futureValue shouldBe Seq.empty[Video]
        }
      }
    }

    "after PUT is called to add a video for a new user" - {
      "calling GET should return a single-element list of videos" in {
        eventually {
          val putResult = firePutRequest(testUserId, testVideoId)
          putResult.futureValue shouldBe StatusCodes.Created

          val getResult          = fireGetRequest(testUserId)
          val videos: Seq[Video] = getResult.futureValue
          videos.length shouldBe 1
          videos.head.userId shouldBe testUserId
          videos.head.videoId shouldBe testVideoId
        }
      }
    }

    "after calling DELETE for a video that exists in a users' list" - {
      "calling GET should return a list of videos without the DELETE'd video" in {
        insertVideoIntoDB(testUserId, testVideoId)
        eventually{
          val deleteResult = fireDeleteRequest(testUserId, testVideoId)
          deleteResult.futureValue shouldBe StatusCodes.NoContent

          val getResult: Future[Seq[Video]] = fireGetRequest(testUserId)
          getResult.futureValue shouldBe Seq.empty[Video]
        }
      }
    }
  }
}
