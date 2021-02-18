package config

import akka.actor.ActorSystem

import scala.concurrent.ExecutionContext

trait ActorSystemConfig {
  implicit val system: ActorSystem        = ActorSystem("my-actor-system")
  implicit val executor: ExecutionContext = system.dispatcher
}
