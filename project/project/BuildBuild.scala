import sbt._
import sbt.Keys._

/**
 *
 * @author mle
 */
object BuildBuild extends Build {
  // "build.sbt" goes here
  override lazy val settings = super.settings ++ Seq(
    // play doesn't like 2.10 here
    scalaVersion := "2.9.2",
    resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    resolvers += "Typesafe ivy releases" at "http://repo.typesafe.com/typesafe/ivy-releases/",
    resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
    scalacOptions ++= Seq("-unchecked", "-deprecation"),
    addSbtPlugin("play" % "sbt-plugin" % "2.1.3-RC1")
  )

  def sbtPlugins = Seq(
    "play" % "sbt-plugin" % "2.1.3-RC1"
  )

  override lazy val projects = Seq(root)
  lazy val root = Project("plugins", file("."))
}

