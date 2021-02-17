package route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, get, path, _}
import akka.http.scaladsl.server.Route
import service.BackendServices._

trait ApiRoutes {
  private val basePath = "videos" / Segment
  val route: Route = concat(
    get {
      path(basePath) { userId =>
        complete(StatusCodes.OK, retrieveAllVideos(userId).toString)
      }
    },
    get {
      path(basePath / Segment) { (userId, numOfRecords) =>
        complete(StatusCodes.OK, retrieveNVideos(userId, numOfRecords.toInt).toString)
      }
    },
    put {
      path(basePath / Segment) { (userId, videoId) =>
        addRecord(userId, videoId)
        complete(StatusCodes.Created, s"$videoId has been added to $userId's videos")
      }
    },
    delete {
      path(basePath / Segment) {  (userId, videoId) =>
        deleteRecord(userId, videoId)
        complete(StatusCodes.NoContent, s"$videoId has been removed to $userId's videos")
      }
    }
  )
}
