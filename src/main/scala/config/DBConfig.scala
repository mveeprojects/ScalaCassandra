package config

import com.datastax.driver.core.Cluster
import com.datastax.oss.driver.api.core.CqlSession
import config.AppConfig.appConfig.cassandra._
import io.getquill.{CamelCase, CassandraAsyncContext}
import utils.Logging

import java.net.InetSocketAddress

object DBConfig extends Logging {

  // the Cluster required by Quill is a class from the legacy Datastax driver (3.7.2, July 2019)
  // https://mvnrepository.com/artifact/com.datastax.cassandra/cassandra-driver-core/3.7.2
  private val quillCluster = Cluster
    .builder()
    .addContactPoint(host)
    .withoutJMXReporting
    .build()

  val quillDB = new CassandraAsyncContext(CamelCase, quillCluster, keyspace, preparedstatementcache)

  def openDBInitSession: (String, Int, String) => CqlSession = (node: String, port: Int, datacentre: String) =>
    CqlSession.builder
      .addContactPoint(new InetSocketAddress(node, port))
      .withLocalDatacenter(datacentre)
      .build

  def closeDBInitSession(session: CqlSession): Unit = {
    logger.info("Closing DB initialisation session.")
    session.close()
  }
}
