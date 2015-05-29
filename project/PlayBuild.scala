import com.mle.sbtplay.PlayProjects
import sbt.Keys._
import sbt._

object PlayBuild extends Build {
  lazy val p = PlayProjects.plainPlayProject("p").settings(commonSettings: _*)

  lazy val commonSettings = Seq(
    organization := "com.github.malliina",
    version := "0.0.1",
    scalaVersion := "2.11.6",
    retrieveManaged := false,
    fork in Test := true,
    updateOptions := updateOptions.value.withCachedResolution(true),
    libraryDependencies ++= Seq(
      "com.github.malliina" %% "play-base" % "0.5.0"),
    exportJars := true,
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    scalacOptions ++= Seq(
      "-target:jvm-1.8",
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
