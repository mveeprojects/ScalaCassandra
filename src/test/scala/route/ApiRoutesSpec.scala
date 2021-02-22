package route

import config.TestConfig.testConf._
import model.Video
import org.scalatest.concurrent.Eventually
import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import utils.HttpUtils._

import scala.concurrent.Future

class ApiRoutesSpec extends AnyFreeSpec with Matchers with Eventually {

  override implicit val patienceConfig: PatienceConfig =
    PatienceConfig(patience.timeout, patience.interval)

  "Api routes" - {
    "when GET is called for a userId that does not exist" - {
      "should return an empty list of videos" in {
        eventually{
          val result: Future[Seq[Video]] = fireGetRequest("unknownuser")
          result.futureValue shouldBe Seq.empty[Video]
        }
      }
    }
  }
}
