import com.malliina.sbtplay.PlayProject
import sbt.Keys._
import sbt._

object PlayBuild extends Build {
  lazy val p = PlayProject("p").settings(commonSettings: _*)

  lazy val commonSettings = Seq(
    organization := "com.malliina",
    version := "0.0.1",
    scalaVersion := "2.11.7",
    retrieveManaged := false,
    fork in Test := true,
    updateOptions := updateOptions.value.withCachedResolution(true),
    resolvers ++= Seq(Resolver.bintrayRepo("malliina", "maven")),
    libraryDependencies ++= Seq("com.malliina" %% "util-play" % "2.6.1"),
    javacOptions ++= Seq(
      "-source", "1.8",
      "-target", "1.8"
    ),
    scalacOptions ++= Seq(
      "-target:jvm-1.8",
      "-encoding", "UTF-8",
      "-unchecked",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-Xlint",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen")
  )
}
