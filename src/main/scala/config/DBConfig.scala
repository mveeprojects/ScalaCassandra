package config

import com.datastax.oss.driver.api.core.CqlSession
import utils.Logging
import config.AppConfig.appConfig._

import java.net.InetSocketAddress

object DBConfig extends Logging {

  def session: CqlSession = {
    CqlSession.builder
      .addContactPoint(new InetSocketAddress(cassandra.host, cassandra.port))
      .withLocalDatacenter(cassandra.datacentre)
      .build
  }

  def closeSession(session: CqlSession): Unit = {
    logger.info("Closing DB session.")
    session.close()
  }
}
