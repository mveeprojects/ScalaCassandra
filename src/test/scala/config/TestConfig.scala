package config

import pureconfig.ConfigSource
import pureconfig.generic.auto._

import scala.concurrent.duration.Duration

object TestConfig {
  case class PatienceConfig(timeout: Duration, interval: Duration)
  case class TestConfig(patience: PatienceConfig)
  val testConf: TestConfig = ConfigSource.default.loadOrThrow[TestConfig]
}
