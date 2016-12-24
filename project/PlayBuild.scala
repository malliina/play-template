import com.malliina.sbtplay.PlayProject
import sbt.Keys._
import sbt._

object PlayBuild {
  lazy val p = PlayProject.default("p").settings(commonSettings: _*)

  lazy val commonSettings = Seq(
    organization := "com.malliina",
    version := "0.0.1",
    scalaVersion := "2.11.8",
    scalacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-unchecked",
      "-feature",
      "-Xlint",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen"
    ),
    resolvers ++= Seq(
      Resolver.jcenterRepo,
      Resolver.bintrayRepo("malliina", "maven")
    ),
    libraryDependencies ++= Seq(
      "com.malliina" %% "util-play" % "3.3.1" % "test->test"
    )
  )
}
