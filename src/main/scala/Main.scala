import akka.http.scaladsl.Http
import config.AppConfig._
import repo.CassandraDB
import route.ApiRoutes
import utils.Logging
import kamon.Kamon

import scala.util.{Failure, Success}

object Main extends App with ApiRoutes with Logging {

  CassandraDB.init()

  Http()
    .newServerAt(appConfig.http.hostname, appConfig.http.port)
    .bindFlow(route)
    .onComplete {
      case Success(_) =>
        Kamon.init
        logger.info(s"App running (${appConfig.http.hostname}:${appConfig.http.port})")
      case Failure(ex) => logger.error(s"App failed to start:\n${ex.getMessage}")
    }
}
