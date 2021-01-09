import com.datastax.oss.driver.api.core.{CqlSession, CqlSessionBuilder}

import java.net.InetSocketAddress

class CassandraConnector {

  val session: CqlSession = CqlSession.builder.build

  def connect(node: String, port: Int, dataCenter: String): CqlSession = {
    val builder: CqlSessionBuilder = CqlSession.builder()
    builder.addContactPoint(new InetSocketAddress(node, port))
    builder.withLocalDatacenter(dataCenter)
    builder.build
  }

  def close(session: CqlSession): Unit =
    session.close()
}
