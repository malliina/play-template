import sbt._
import sbt.Keys._

object PlayBuild extends Build {

  lazy val p = Project("p", file(".")).enablePlugins(play.PlayScala).settings(commonSettings: _*)

  val commonSettings = Seq(
    scalaVersion := "2.11.1",
    retrieveManaged := false,
    fork in Test := true,
    resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.2.0" % "test"
    )
  )
}