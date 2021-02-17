package config

import com.datastax.driver.core.Cluster
import com.datastax.oss.driver.api.core.CqlSession
import config.AppConfig.appConfig.cassandra._
import io.getquill.{CamelCase, CassandraAsyncContext}

import java.net.InetSocketAddress

trait DBConfig {

  private lazy val cluster = Cluster
    .builder()
    .addContactPoint("cassandra")
    .withoutJMXReporting
    .build()

  lazy val db = new CassandraAsyncContext(CamelCase, cluster, keyspace, preparedstatementcache)

  val setupSession: (String, Int, String) => CqlSession = (node: String, port: Int, datacentre: String) =>
    CqlSession.builder
      .addContactPoint(new InetSocketAddress(node, port))
      .withLocalDatacenter(datacentre)
      .build

  def close(session: CqlSession): Unit =
    session.close()
}
