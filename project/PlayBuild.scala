import com.malliina.sbtplay.PlayProject
import sbt.Keys._
import sbt._

object PlayBuild {
  lazy val p = PlayProject("p").settings(commonSettings: _*)

  lazy val commonSettings = Seq(
    organization := "com.malliina",
    version := "0.0.1",
    scalaVersion := "2.11.8",
    scalacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-unchecked",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-Xlint",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen"),
    libraryDependencies ++= Seq(
      "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
    )
  )
}
