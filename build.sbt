import Aliases.customAliases
import Dependencies._
import Monitoring.kamonSettings

name := "ScalaCassandra"

version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies ++= (
  mvpCommon ++
    cassandraDependencies ++
    akkaDependencies ++
    loggingDependencies ++
    monitoringDependencies ++
    configDependencies ++
    testDependencies
)

lazy val root = Project("ScalaCassandra", file("."))
  .settings(customAliases)
  .settings(kamonSettings)
  .enablePlugins(JavaAppPackaging, DockerComposePlugin, JavaAgent)

dockerImageCreationTask := (publishLocal in Docker).value
