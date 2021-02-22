import sbt._

object Dependencies {

  object Versions {
    val akkaVersion       = "2.6.12"
    val akkaHttpVersion   = "10.2.3"
    val logbackVersion    = "1.2.3"
    val pureconfigVersion = "0.14.0"
    val cassandraDriver   = "4.9.0"
    val quill             = "3.6.1"
    val scalaTestVersion  = "3.2.2"
  }

  import Versions._

  val cassandraDependencies = Seq(
    "com.datastax.oss" % "java-driver-core"          % cassandraDriver,
    "com.datastax.oss" % "java-driver-query-builder" % cassandraDriver,
    "io.getquill"     %% "quill-cassandra"           % quill
  )

  val akkaDependencies = Seq(
    "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream"          % akkaVersion,
    "com.typesafe.akka" %% "akka-actor"           % akkaVersion
  )

  val loggingDependencies = Seq(
    "ch.qos.logback" % "logback-classic" % logbackVersion
  )

  val configDependencies = Seq(
    "com.github.pureconfig" %% "pureconfig" % pureconfigVersion
  )

  val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % scalaTestVersion % Test
  )
}
