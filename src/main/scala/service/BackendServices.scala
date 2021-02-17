package service

import model.VideoDBEntry
import repo.initDB.videoRepository
import service.MyProtocol._
import spray.json.{JsValue, enrichAny}

import java.time.Instant

object BackendServices {
  def retrieveAllVideos(userId: String): JsValue       = videoRepository.selectAllForUser(userId).toJson
  def retrieveNVideos(userId: String, n: Int): JsValue = videoRepository.selectFirstNForUser(userId, n).toJson
  def addRecord(userId: String, videoId: String): Unit =
    videoRepository.insertVideo(VideoDBEntry(userId, videoId, "video title", Instant.now()))
}
