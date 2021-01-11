package config

import pureconfig.ConfigSource
import pureconfig.generic.auto._

object Configuration {

  val appConfig: AppConfig = ConfigSource.default.loadOrThrow[AppConfig]

  case class CassandraConfig(
      node: String,
      port: Int,
      datacentre: String,
      keyspace: String,
      replicas: Int,
      tablename: String,
      lingerSeconds: Option[Int]
  )

  case class AppConfig(cassandra: CassandraConfig)
}
