package route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, get, path, _}
import akka.http.scaladsl.server.Route

import service.BackendServices._

trait ApiRoutes {
  val route: Route = concat(
    get {
      path("videos") {
        complete(StatusCodes.OK, retrieveAllVideos.toString)
      }
    }
  )
}
