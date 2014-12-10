import sbt._
import sbt.Keys._

/**
 *
 * @author mle
 */
object BuildBuild extends Build {
  // "build.sbt" goes here
  override lazy val settings = super.settings ++ Seq(
    scalaVersion := "2.10.4",
    resolvers ++= Seq(
      "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
      "Typesafe ivy releases" at "http://repo.typesafe.com/typesafe/ivy-releases/"
    ),
    scalacOptions ++= Seq("-unchecked", "-deprecation")
//    libraryDependencies ++= Seq("com.github.malliina" %% "sbt-appbundler" % "0.0.10")
  ) ++ sbtPlugins

  def sbtPlugins = Seq(
    "com.typesafe.play" % "sbt-plugin" % "2.3.6",
    "com.github.malliina" % "sbt-play" % "0.1.1",
    "com.github.malliina" % "sbt-packager" % "1.5.10"
  ) map addSbtPlugin

  override lazy val projects = Seq(root)
  lazy val root = Project("plugins", file("."))
}

