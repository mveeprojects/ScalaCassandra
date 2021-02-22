import Aliases.customAliases
import Dependencies._

name := "ScalaCassandra"

version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies ++= (
  cassandraDependencies ++
    akkaDependencies ++
    loggingDependencies ++
    configDependencies ++
    testDependencies
)

lazy val root = Project("ScalaCassandra", file("."))
  .settings(customAliases)
  .enablePlugins(JavaAppPackaging, DockerComposePlugin)

dockerImageCreationTask := (publishLocal in Docker).value
