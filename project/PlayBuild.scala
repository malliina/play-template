import sbt._
import sbt.Keys._

object PlayBuild extends Build {

  lazy val p = Project("p", file(".")).settings(projectSettings: _*)

  val projectSettings = Seq(
    scalaVersion := "2.10.3",
    retrieveManaged := false,
    fork in Test := true,
    resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.0" % "test"
    )
  )
}