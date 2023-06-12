package config

import com.datastax.oss.driver.api.core.CqlSession
import utils.Logging

import java.net.InetSocketAddress

object DBConfig extends AppConfig with Logging {

  val session: CqlSession = {
    CqlSession.builder
      .addContactPoint(new InetSocketAddress(cassandraConfig.cassandra.host, cassandraConfig.cassandra.port))
      .withLocalDatacenter(cassandraConfig.cassandra.datacentre)
      .build
  }

  def closeSession(session: CqlSession): Unit = {
    logger.info("Closing DB session.")
    session.close()
  }
}
