package repo

import com.datastax.oss.driver.api.core.cql.ResultSet
import com.datastax.oss.driver.api.core.{CqlIdentifier, CqlSession}
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace

class KeyspaceRepository(session: CqlSession) {

  def createKeyspaceIfNotExists(keyspaceName: String, numberOfReplicas: Int): Unit = {
    val cks: CreateKeyspace = SchemaBuilder
      .createKeyspace(keyspaceName)
      .ifNotExists
      .withSimpleStrategy(numberOfReplicas)
    session.execute(cks.build)
  }

  def useKeyspace(keyspace: String): ResultSet =
    session.execute("USE " + CqlIdentifier.fromCql(keyspace))
}
