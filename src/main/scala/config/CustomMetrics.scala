package config

import kamon.Kamon
import kamon.metric.Gauge

object CustomMetrics {
  val cassandraReachable: Gauge = Kamon.gauge("cassandra-reachable").withoutTags()
}
