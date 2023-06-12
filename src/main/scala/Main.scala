import akka.http.scaladsl.Http
import config.AppConfig
import route.ApiRoutes
import utils.Logging
import kamon.Kamon

import scala.util.{Failure, Success}

object Main extends App with AppConfig with ApiRoutes with Logging {

  Http()
    .newServerAt(httpConfig.http.hostname, httpConfig.http.port)
    .bindFlow(route)
    .onComplete {
      case Success(_) =>
        Kamon.init
        logger.info(s"App running (${httpConfig.http.hostname}:${httpConfig.http.port})")
      case Failure(ex) => logger.error(s"App failed to start:\n${ex.getMessage}")
    }
}
