package repo

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.internal.core.metadata.SniEndPoint

import java.net.InetSocketAddress

class CassandraConnector {

  val setupSession: (String, Int, String) => CqlSession = (node: String, port: Int, datacentre: String) =>
    CqlSession.builder
      .addContactEndPoint(new SniEndPoint(new InetSocketAddress(node, port), "big_cass"))
      .withLocalDatacenter(datacentre)
      .build

  def connect(node: String, port: Int, dataCenter: String): CqlSession =
    CqlSession
      .builder()
      .addContactPoint(new InetSocketAddress(node, port))
      .withLocalDatacenter(dataCenter)
      .build

  def close(session: CqlSession): Unit =
    session.close()
}
