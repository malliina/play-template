import com.mle.play.PlayProjects
import sbt.Keys._
import sbt._

object PlayBuild extends Build {

  lazy val p = PlayProjects.playProject("p").settings(commonSettings: _*)

  val commonSettings = Seq(
    scalaVersion := "2.11.2",
    retrieveManaged := false,
    fork in Test := true
  )
}