import com.mle.sbtplay.PlayProjects
import sbt.Keys._
import sbt._

object PlayBuild extends Build {

  lazy val p = PlayProjects.plainPlayProject("p").settings(commonSettings: _*)

  val commonSettings = Seq(
    scalaVersion := "2.11.2",
    retrieveManaged := false,
    fork in Test := true
  )
}