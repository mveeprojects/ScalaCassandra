package service

import repo.initDB.videoRepository
import service.MyProtocol._
import spray.json.{JsValue, enrichAny}

object BackendServices {
  def retrieveAllVideos: JsValue = videoRepository.selectAll.toJson
}
