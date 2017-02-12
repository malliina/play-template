import sbt.Keys._
import sbt._

object BuildBuild {
  val settings = sbtPlugins ++ Seq(
    scalaVersion := "2.10.6",
    resolvers ++= Seq(
      ivyRepo("malliina bintray sbt", "https://dl.bintray.com/malliina/sbt-plugins/")
    ),
    scalacOptions ++= Seq("-unchecked", "-deprecation")
  )

  def ivyRepo(name: String, urlString: String) =
    Resolver.url(name, url(urlString))(Resolver.ivyStylePatterns)

  def sbtPlugins = Seq(
    "com.malliina" % "sbt-play" % "0.9.2"
  ) map addSbtPlugin
}
