package route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, get, path, _}
import akka.http.scaladsl.server.Route
import config.AppConfig._
import model.VideoProtocol._
import service.BackendServices._
import spray.json._

trait ApiRoutes {
  private val basePath = "videos" / Segment
  val route: Route = concat(
    get {
      path(basePath) { userId =>
        val result = retrieveAllVideos(userId).map(_.toJson)
        complete(StatusCodes.OK, result)
      }
    },
    get {
      path(basePath / Segment) { (userId, numOfRecords) =>
        val result = retrieveNVideos(userId, numOfRecords.toInt).map(_.toJson)
        complete(StatusCodes.OK, result)
      }
    },
    put {
      path(basePath / Segment) { (userId, videoId) =>
        addRecord(userId, videoId)
        complete(StatusCodes.Created, s"$videoId has been added to $userId's videos")
      }
    },
    delete {
      path(basePath / Segment) { (userId, videoId) =>
        deleteRecord(userId, videoId)
        complete(StatusCodes.NoContent)
      }
    },
    get {
      path("readiness") {
        complete(StatusCodes.Accepted)
      }
    }
  )
}
