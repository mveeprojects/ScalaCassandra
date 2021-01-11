import Dependencies._

name := "ScalaCassandra"

version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies ++= (
  cassandraDependencies ++
    loggingDependencies ++
    configDependencies
)

enablePlugins(JavaAppPackaging, DockerComposePlugin)

dockerImageCreationTask := (publishLocal in Docker).value
