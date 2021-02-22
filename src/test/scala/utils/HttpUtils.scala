package utils

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import model.Video
import model.VideoProtocol._

import scala.concurrent.{ExecutionContext, Future}

object HttpUtils {

  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContext     = actorSystem.dispatcher

  def fireGetRequest(userId: String): Future[Seq[Video]] =
    Http()
      .singleRequest(HttpRequest(HttpMethods.GET, s"http://localhost:80/videos/$userId"))
      .flatMap(response =>
        Unmarshal(response).to[Seq[Video]].collect { case vids =>
          vids
        }
      )
}
