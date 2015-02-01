import com.mle.sbtplay.PlayProjects
import sbt.Keys._
import sbt._

object PlayBuild extends Build {
  lazy val p = PlayProjects.plainPlayProject("p").settings(commonSettings: _*)

  lazy val commonSettings = Seq(
    organization := "com.github.malliina",
    version := "0.2.1",
    scalaVersion := "2.11.5",
    retrieveManaged := false,
    fork in Test := true,
    updateOptions := updateOptions.value.withCachedResolution(true),
    libraryDependencies ++= Seq(
      "com.github.malliina" %% "play-base" % "0.2.2"),
    mainClass := Some("com.mle.play.Starter"),
    exportJars := true,
    javacOptions ++= Seq("-source", "1.7", "-target", "1.7"),
    scalacOptions ++= Seq(
      "-target:jvm-1.7",
      "-deprecation",
      "-encoding", "UTF-8",
      "-unchecked",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-Xfatal-warnings",
      "-Xlint",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen")
  )
}