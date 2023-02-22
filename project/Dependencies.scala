import sbt._

object Dependencies {

  object Versions {
    val akka            = "2.6.12"
    val akkaHttp        = "10.2.3"
    val logback         = "1.2.3"
    val pureConfig      = "0.14.0"
    val cassandraDriver = "4.15.0"
    val scalaTest       = "3.2.2"
    val kamon           = "2.1.12"
    val kanela          = "1.0.7"
  }

  import Versions._

  val cassandraDependencies = Seq(
    "com.datastax.oss" % "java-driver-core"          % cassandraDriver,
    "com.datastax.oss" % "java-driver-query-builder" % cassandraDriver
  )

  val akkaDependencies = Seq(
    "com.typesafe.akka" %% "akka-http"            % akkaHttp,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttp,
    "com.typesafe.akka" %% "akka-stream"          % akka,
    "com.typesafe.akka" %% "akka-actor"           % akka
  )

  val loggingDependencies = Seq(
    "ch.qos.logback" % "logback-classic" % logback
  )

  val monitoringDependencies = Seq(
    "io.kamon" %% "kamon-core"           % kamon,
    "io.kamon" %% "kamon-akka-http"      % kamon,
    "io.kamon" %% "kamon-system-metrics" % kamon,
    "io.kamon" %% "kamon-prometheus"     % kamon,
    "io.kamon"  % "kanela-agent"         % kanela
  )

  val configDependencies = Seq(
    "com.github.pureconfig" %% "pureconfig" % pureConfig
  )

  val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % scalaTest % Test
  )
}
