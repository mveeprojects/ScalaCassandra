package config

import pureconfig.ConfigSource
import pureconfig.generic.auto._

object AppConfig extends ActorSystemConfig with DBConfig {

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

  case class Config(http: HttpConfig, cassandra: CassandraConfig)

  val appConfig: Config = ConfigSource.default.loadOrThrow[Config]
}
