package config

import pureconfig.ConfigSource
import pureconfig.generic.auto._

object AppConfig extends ActorSystemConfig {

  case class HttpConfig(hostname: String, port: Int)

  case class CassandraConfig(
      node: String,
      port: Int,
      datacentre: String,
      keyspace: String,
      replicas: Int,
      tablename: String,
      lingerSeconds: Option[Int]
  )

  case class AppConfig(http: HttpConfig, cassandra: CassandraConfig)

  val appConfig: AppConfig = ConfigSource.default.loadOrThrow[AppConfig]
}