import com.malliina.sbtplay.PlayProject

val utilPlayDep = "com.malliina" %% "util-play" % "4.12.2"

lazy val backend = PlayProject.default("backend")
  .settings(backendSettings: _*)
  .dependsOn(crossJvm)

lazy val frontend = project.in(file("frontend"))
  .settings(frontendSettings: _*)
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(crossJs)

lazy val cross = crossProject.in(file("shared"))
  .settings(commonSettings: _*)

lazy val crossJvm = cross.jvm
lazy val crossJs = cross.js

lazy val backendSettings = commonSettings ++ Seq(
  resolvers ++= Seq(
    Resolver.jcenterRepo,
    Resolver.bintrayRepo("malliina", "maven")
  ),
  libraryDependencies ++= Seq(
    utilPlayDep,
    utilPlayDep % Test classifier "tests"
  ),
  dependencyOverrides ++= Seq(
    "com.typesafe.akka" %% "akka-stream" % "2.5.8",
    "com.typesafe.akka" %% "akka-actor" % "2.5.8"
  ),
  pipelineStages := Seq(digest, gzip),
  scalaJSProjects := Seq(frontend),
  pipelineStages in Assets := Seq(scalaJSPipeline)
)

lazy val frontendSettings = commonSettings ++ Seq(
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.2",
    "org.scalatest" %%% "scalatest" % "3.0.4" % Test,
    "be.doeraene" %%% "scalajs-jquery" % "0.9.2"
  ),
  scalaJSUseMainModuleInitializer := true
)

lazy val commonSettings = Seq(
  organization := "com.malliina",
  version := "0.0.1",
  scalaVersion := "2.12.6",
  scalacOptions := Seq("-unchecked", "-deprecation")
)
