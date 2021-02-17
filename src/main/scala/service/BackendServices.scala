package service

import model.Video
import repo.initDB.videoRepository

import java.time.Instant
import scala.concurrent.Future

object BackendServices {

  def retrieveAllVideos(userId: String): Future[List[Video]] =
    videoRepository.selectAllForUser(userId)

  def retrieveNVideos(userId: String, n: Int): Future[List[Video]] =
    videoRepository.selectFirstNForUser(userId, n)

  def addRecord(userId: String, videoId: String): Unit =
    videoRepository.insertVideoForUser(Video(userId, videoId, "video title", Instant.now()))

  def deleteRecord(userId: String, videoId: String): Unit =
    videoRepository.deleteVideoForUser(userId, videoId)
}
