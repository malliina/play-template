import sbt._
import sbt.Keys._

object PlayBuild extends Build {

  import Dependencies._

  lazy val p = play.Project("p",
    path = file("."),
    applicationVersion = "0.1",
    dependencies = Seq(scalaTest),
    settings = commonSettings
  )

  // Hack for play compat
  override def settings = super.settings ++ com.typesafe.sbtidea.SbtIdeaPlugin.ideaSettings

  val commonSettings = Defaults.defaultSettings ++ Seq(
    scalaVersion := "2.10.2",
    retrieveManaged := false,
    fork in Test := true,
    resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
  )
}

object Dependencies {
  val scalaTest = "org.scalatest" %% "scalatest" % "1.9.1" % "test"
}