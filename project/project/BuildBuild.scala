import sbt._
import sbt.Keys._

object BuildBuild extends Build {
  // "build.sbt" goes here
  override lazy val settings = super.settings ++ Seq(
    scalaVersion := "2.10.6",
    resolvers ++= Seq(
      Resolver.url("malliina bintray sbt", url("https://dl.bintray.com/malliina/sbt-plugins/"))(Resolver.ivyStylePatterns)
    ),
    scalacOptions ++= Seq("-unchecked", "-deprecation")
  ) ++ sbtPlugins

  def sbtPlugins = Seq(
    "com.typesafe.play" % "sbt-plugin" % "2.5.5",
    "com.malliina" % "sbt-play" % "0.8.0"
  ) map addSbtPlugin

  override lazy val projects = Seq(root)
  lazy val root = Project("plugins", file("."))
}
