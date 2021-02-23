import com.lightbend.sbt.javaagent.JavaAgent
import com.lightbend.sbt.javaagent.JavaAgent.JavaAgentKeys._
import sbt._

object Monitoring {
  val kamonSettings = Seq(
    javaAgents += JavaAgent("io.kamon" % "kanela-agent" % "1.0.7")
  )
}
