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

  private val baseUrl    = "http://localhost:80"
  private val apiBaseUrl = s"$baseUrl/videos"

  def fireGetRequest(userId: String): Future[Seq[Video]] =
    Http()
      .singleRequest(HttpRequest(HttpMethods.GET, s"$apiBaseUrl/$userId"))
      .flatMap(response =>
        Unmarshal(response).to[Seq[Video]].collect { case vids =>
          vids
        }
      )

  def firePutRequest(userId: String, videoId: String): Future[StatusCode] =
    Http()
      .singleRequest(HttpRequest(HttpMethods.PUT, s"$apiBaseUrl/$userId/$videoId"))
      .map(_.status)

  def fireDeleteRequest(userId: String, videoId: String): Future[StatusCode] =
    Http()
      .singleRequest(HttpRequest(HttpMethods.DELETE, s"$apiBaseUrl/$userId/$videoId"))
      .map(_.status)
}
