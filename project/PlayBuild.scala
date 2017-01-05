import com.malliina.sbtplay.PlayProject
import sbt.Keys._
import sbt._

object PlayBuild {
  lazy val p = PlayProject.default("p").settings(commonSettings: _*)

  val utilPlayDep = "com.malliina" %% "util-play" % "3.3.3"

  lazy val commonSettings = Seq(
    organization := "com.malliina",
    version := "0.0.1",
    scalaVersion := "2.11.8",
    resolvers ++= Seq(
      Resolver.jcenterRepo,
      Resolver.bintrayRepo("malliina", "maven")
    ),
    libraryDependencies ++= Seq(
      utilPlayDep,
      utilPlayDep % Test classifier "tests"
    )
  )
}
