import akka.http.scaladsl.Http
import config.AppConfig._
import repo.initDB
import route.ApiRoutes
import utils.Logging

import scala.util.{Failure, Success}

object Main extends App with ApiRoutes with Logging {

  initDB.init

  Http()
    .newServerAt(appConfig.http.hostname, appConfig.http.port)
    .bindFlow(route)
    .onComplete {
      case Success(_) =>
        logger.info(s"App running (${appConfig.http.hostname}:${appConfig.http.port})")
      case Failure(ex) => logger.error(s"App failed to start:\n${ex.getMessage}")
    }
}
