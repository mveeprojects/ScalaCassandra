package repo

import com.datastax.oss.driver.api.core.CqlSession

import java.net.InetSocketAddress

class CassandraConnector {

  val setupSession: (String, Int, String) => CqlSession = (node: String, port: Int, datacentre: String) =>
    CqlSession.builder
      .addContactPoint(new InetSocketAddress(node, port))
      .withLocalDatacenter(datacentre)
      .build

  def close(session: CqlSession): Unit =
    session.close()
}
