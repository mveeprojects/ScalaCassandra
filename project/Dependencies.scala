import sbt._

object Dependencies {

  import Versions._

  val cassandraDependencies = Seq(
    "com.datastax.oss" % "java-driver-core"          % cassandraDriver,
    "com.datastax.oss" % "java-driver-query-builder" % cassandraDriver
  )

  val loggingDependencies = Seq(
    "ch.qos.logback" % "logback-classic" % logbackVersion
  )

  val configDependencies = Seq(
    "com.github.pureconfig" %% "pureconfig" % pureconfigVersion
  )

  object Versions {
    val cassandraDriver   = "4.9.0"
    val logbackVersion    = "1.2.3"
    val pureconfigVersion = "0.14.0"
  }
}
