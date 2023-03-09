package service

import model.Video
import repo.VideoRepository

import java.time.Instant
import java.util.UUID
import scala.concurrent.Future

object BackendServices {

  private val videoRepository: VideoRepository = new VideoRepository()

  def retrieveAllVideos(userId: String): Future[List[Video]] =
    videoRepository.selectAllForUser(userId)

  def retrieveNVideos(userId: String, n: Int): Future[List[Video]] =
    videoRepository.selectFirstNForUser(userId, n)

  def addRecord(userId: String, videoId: String): Unit =
    videoRepository.insertVideoForUser(Video(UUID.fromString(userId), videoId, Some("video title"), Instant.now()))

  def deleteRecord(userId: String, videoId: String): Unit =
    videoRepository.deleteVideoForUser(userId, videoId)
}
