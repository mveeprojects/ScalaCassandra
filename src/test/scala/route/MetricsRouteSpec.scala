package route

import base.FuncSpecBase
import config.TestConfig.testConf._
import utils.HttpUtils.getMetricsAsList

class MetricsRouteSpec extends FuncSpecBase {

  override implicit val patienceConfig: PatienceConfig =
    PatienceConfig(patience.timeout, patience.interval)

  "Metrics route" - {
    "should contain custom metric" - {
      "cassandra_reachable with a value of 1.0" in {
        eventually {
          val getMetricsResult: List[String] = getMetricsAsList
          getMetricsResult should contain("cassandra_reachable 1.0")
        }
      }
    }
  }
}
