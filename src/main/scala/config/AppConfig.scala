package config

import com.datastax.driver.core.Cluster
import io.getquill.{CamelCase, CassandraAsyncContext}
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

  case class Config(http: HttpConfig, cassandra: CassandraConfig)

  val appConfig: Config = ConfigSource.default.loadOrThrow[Config]

  private lazy val cluster = Cluster
    .builder()
    .addContactPoint("cassandra")
    .withoutJMXReporting
    .build()

  lazy val db = new CassandraAsyncContext(CamelCase, cluster, appConfig.cassandra.keyspace, 100)
}
