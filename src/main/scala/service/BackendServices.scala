package service

import model.Video
import repo.CassandraDB.videoRepository._

import java.time.Instant
import scala.concurrent.Future

object BackendServices {

  def retrieveAllVideos(userId: String): Future[List[Video]] =
    selectAllForUser(userId)

  def retrieveNVideos(userId: String, n: Int): Future[List[Video]] =
    selectFirstNForUser(userId, n)

  def addRecord(userId: String, videoId: String): Unit =
    insertVideoForUser(Video(userId, videoId, "video title", Instant.now()))

  def deleteRecord(userId: String, videoId: String): Unit =
    deleteVideoForUser(userId, videoId)
}
