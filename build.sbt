import com.malliina.sbt.GenericKeys
import com.malliina.sbt.GenericKeys.pkgHome
import com.malliina.sbt.win.WinKeys.{forceStopOnUninstall, upgradeGuid, useTerminateProcess, winSwExe}
import com.malliina.sbt.win.WinPlugin
import com.malliina.sbtplay.PlayProject
import sbt.Keys.fork
import sbtcrossproject.CrossPlugin.autoImport.{CrossType => PortableType, crossProject => portableProject}

import scala.sys.process.Process
import scala.util.Try

val utilPlayDep = "com.malliina" %% "util-play" % "4.17.0"

lazy val all = project.in(file("."))
  .aggregate(vanilla, backend, frontend, crossJvm, crossJs, native)

lazy val vanilla = PlayProject.default("vanilla", file("vanilla"))
  .settings(vanillaSettings: _*)

lazy val backend = PlayProject.default("backend", file("full"))
  .settings(backendSettings: _*)
  .dependsOn(crossJvm)

lazy val frontend = project.in(file("full/frontend"))
  .settings(frontendSettings: _*)
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(crossJs)

lazy val cross = portableProject(JSPlatform, JVMPlatform)
  .crossType(PortableType.Full)
  .in(file("full/shared"))
  .settings(commonSettings: _*)

lazy val crossJvm = cross.jvm
lazy val crossJs = cross.js

lazy val native = project.in(file("native"))
  .enablePlugins(PlayScala, SbtNativePackager, BuildInfoPlugin)
  .settings(nativeSettings: _*)

lazy val vanillaSettings: Seq[Setting[_]] = commonSettings ++ Seq(
  libraryDependencies ++= Seq(
    utilPlayDep,
    utilPlayDep % Test classifier "tests"
  ),
  pipelineStages := Seq(digest, gzip)
)

lazy val nativeSettings: Seq[Setting[_]] = commonSettings ++ WinPlugin.windowsSettings ++ Seq(
  retrieveManaged := false,
  fork in Test := true,
  libraryDependencies ++= Seq(
    "com.lihaoyi" %% "scalatags" % "0.6.7"
  ),
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
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-numeric-widen"
  ),
  upgradeGuid := "5EC7F244-24F9-4E1C-B19D-591626C50F02",
  GenericKeys.manufacturer := "Me",
  forceStopOnUninstall := true,
  winSwExe in Windows := (pkgHome in Windows).value.resolve("WinSW.NET2.exe"),
  useTerminateProcess := true,
  buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, "gitHash" -> gitHash),
  buildInfoPackage := "com.malliina.pn"
)

lazy val backendSettings = commonSettings ++ Seq(
  libraryDependencies ++= Seq(
    utilPlayDep,
    utilPlayDep % Test classifier "tests"
  ),
  pipelineStages := Seq(digest, gzip),
  scalaJSProjects := Seq(frontend),
  pipelineStages in Assets := Seq(scalaJSPipeline)
)

lazy val frontendSettings = commonSettings ++ Seq(
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.6",
    "org.scalatest" %%% "scalatest" % "3.0.5" % Test,
    "be.doeraene" %%% "scalajs-jquery" % "0.9.4"
  ),
  scalaJSUseMainModuleInitializer := true
)

lazy val commonSettings = Seq(
  organization := "com.malliina",
  version := "0.0.1",
  scalaVersion := "2.12.6",
  scalacOptions := Seq("-unchecked", "-deprecation"),
  resolvers ++= Seq(
    Resolver.jcenterRepo,
    Resolver.bintrayRepo("malliina", "maven")
  ),
  dependencyOverrides ++= Seq(
    "com.typesafe.akka" %% "akka-stream" % "2.5.8",
    "com.typesafe.akka" %% "akka-actor" % "2.5.8"
  )
)

def gitHash: String =
  Try(Process("git rev-parse --short HEAD").lineStream.head).toOption.getOrElse("unknown")
