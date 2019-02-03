import com.malliina.sbt.GenericKeys
import com.malliina.sbt.GenericKeys.pkgHome
import com.malliina.sbt.win.WinKeys.{forceStopOnUninstall, upgradeGuid, useTerminateProcess, winSwExe}
import com.malliina.sbt.win.WinPlugin
import sbt.Keys.fork
import sbtcrossproject.CrossPlugin.autoImport.{CrossType => PortableType, crossProject => portableProject}

import scala.sys.process.Process
import scala.util.Try

val utilPlayDep = "com.malliina" %% "util-play" % "5.0.0"

val commonSettings = Seq(
  organization := "com.malliina",
  version := "0.0.1",
  scalaVersion := "2.12.8",
  scalacOptions := Seq("-unchecked", "-deprecation"),
  resolvers ++= Seq(
    Resolver.jcenterRepo,
    Resolver.bintrayRepo("malliina", "maven")
  )
)

val vanilla = project.in(file("vanilla"))
  .enablePlugins(PlayDefaultPlugin)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      utilPlayDep,
      utilPlayDep % Test classifier "tests"
    ),
    pipelineStages := Seq(digest, gzip)
  )

val cross = portableProject(JSPlatform, JVMPlatform)
  .crossType(PortableType.Full)
  .in(file("shared"))
  .settings(commonSettings: _*)

val crossJvm = cross.jvm
val crossJs = cross.js

val frontend = project.in(file("frontend"))
  .enablePlugins(ScalaJSBundlerPlugin, ScalaJSWeb)
  .dependsOn(crossJs)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "scalatags" % "0.6.7",
      "org.scala-js" %%% "scalajs-dom" % "0.9.6",
      "org.scalatest" %%% "scalatest" % "3.0.5" % Test,
      "be.doeraene" %%% "scalajs-jquery" % "0.9.4"
    ),
    scalaJSUseMainModuleInitializer := true,
    version in webpack := "4.27.1",
    emitSourceMaps := false,
    webpackBundlingMode := BundlingMode.LibraryOnly(),
    npmDevDependencies in Compile ++= Seq(
      // Hack because webpack sucks
      "terser" -> "3.14.1",
      "webpack-merge" -> "4.1.5"
    ),
    webpackConfigFile in fastOptJS := Some(baseDirectory.value / "webpack.dev.config.js"),
    webpackConfigFile in fullOptJS := Some(baseDirectory.value / "webpack.prod.config.js"),
  )

val backend = project.in(file("backend"))
  .enablePlugins(WebScalaJSBundlerPlugin, PlayDefaultPlugin)
  .dependsOn(crossJvm)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      utilPlayDep,
      utilPlayDep % Test classifier "tests"
    ),
    pipelineStages := Seq(digest, gzip),
    scalaJSProjects := Seq(frontend),
    pipelineStages in Assets := Seq(scalaJSPipeline)
  )

val native = project.in(file("native"))
  .enablePlugins(PlayScala, SbtNativePackager, BuildInfoPlugin)
  .settings(commonSettings ++ WinPlugin.windowsSettings)
  .settings(
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

val playTemplates = project.in(file("."))
  .aggregate(vanilla, frontend, backend, native)
  .settings(commonSettings)

def gitHash: String =
  Try(Process("git rev-parse --short HEAD").lineStream.head).toOption.getOrElse("unknown")
