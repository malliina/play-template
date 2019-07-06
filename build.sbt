import com.malliina.sbt.GenericKeys
import com.malliina.sbt.GenericKeys.pkgHome
import com.malliina.sbt.win.WinKeys.{
  forceStopOnUninstall,
  upgradeGuid,
  useTerminateProcess,
  winSwExe
}
import com.malliina.sbt.win.WinPlugin
import sbt.Keys.fork
import sbtcrossproject.CrossPlugin.autoImport.{
  CrossType => PortableType,
  crossProject => portableProject
}

import scala.sys.process.Process
import scala.util.Try

val utilPlayDep = "com.malliina" %% "util-play" % "5.2.3"
val scalaTestDep = "org.scalatest" %% "scalatest" % "3.0.8" % Test
val scalaTestPlusDep = "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test

val commonSettings = Seq(
  organization := "com.malliina",
  version := "0.0.1",
  scalaVersion := "2.13.0",
  scalacOptions := Seq("-unchecked", "-deprecation"),
  resolvers ++= Seq(
    Resolver.jcenterRepo,
    Resolver.bintrayRepo("malliina", "maven")
  )
)

val backendSettings = Seq(
  libraryDependencies ++= Seq(
    utilPlayDep,
    utilPlayDep % Test classifier "tests",
    scalaTestDep,
    scalaTestPlusDep
  )
)

val vanilla = project
  .in(file("vanilla"))
  .enablePlugins(PlayScala)
  .settings(commonSettings ++ backendSettings)
  .settings(
    pipelineStages := Seq(digest, gzip)
  )

val cross = portableProject(JSPlatform, JVMPlatform)
  .crossType(PortableType.Full)
  .in(file("shared"))
  .settings(commonSettings: _*)

val crossJvm = cross.jvm
val crossJs = cross.js

val ncu = taskKey[Int]("Runs npm-check-updates")
val frontend = project
  .in(file("frontend"))
  .enablePlugins(ScalaJSBundlerPlugin, ScalaJSWeb)
  .dependsOn(crossJs)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "scalatags" % "0.7.0",
      "org.scala-js" %%% "scalajs-dom" % "0.9.7",
      "be.doeraene" %%% "scalajs-jquery" % "0.9.5",
      "org.scalatest" %%% "scalatest" % "3.0.8" % Test
    ),
    scalaJSUseMainModuleInitializer := true,
    emitSourceMaps := false,
    webpackBundlingMode := BundlingMode.LibraryOnly(),
    npmDependencies in Compile ++= Seq(
      "@fortawesome/fontawesome-free" -> "5.9.0",
      "bootstrap" -> "4.3.1",
      "jquery" -> "3.4.1",
      "popper.js" -> "1.15.0"
    ),
    npmDevDependencies in Compile ++= Seq(
      "autoprefixer" -> "9.6.0",
      "cssnano" -> "4.1.10",
      "css-loader" -> "3.0.0",
      "file-loader" -> "4.0.0",
      "less" -> "3.9.0",
      "less-loader" -> "5.0.0",
      "mini-css-extract-plugin" -> "0.6.0",
      "postcss-import" -> "12.0.1",
      "postcss-loader" -> "3.0.0",
      "postcss-preset-env" -> "6.6.0",
      "style-loader" -> "0.23.1",
      "url-loader" -> "2.0.1",
      "webpack-merge" -> "4.2.1"
    ),
    version in webpack := "4.35.2",
    webpackConfigFile in fastOptJS := Some(baseDirectory.value / "webpack.dev.config.js"),
    webpackConfigFile in fullOptJS := Some(baseDirectory.value / "webpack.prod.config.js"),
    ncu := {
      val log = streams.value.log
      val cwd = (crossTarget in (Compile, npmUpdate)).value
      log.info(s"Running 'ncu' in $cwd...")
      Process("ncu", cwd).run(log).exitValue()
    }
//    dependencyUpdates := dependencyUpdates.dependsOn(ncu).value
  )

val backend = project
  .in(file("backend"))
  .enablePlugins(WebScalaJSBundlerPlugin, PlayScala)
  .dependsOn(crossJvm)
  .settings(commonSettings ++ backendSettings)
  .settings(
    pipelineStages := Seq(digest, gzip),
    scalaJSProjects := Seq(frontend),
    pipelineStages in Assets := Seq(scalaJSPipeline)
  )

val native = project
  .in(file("native"))
  .enablePlugins(PlayScala, SbtNativePackager, BuildInfoPlugin)
  .settings(commonSettings ++ backendSettings ++ WinPlugin.windowsSettings)
  .settings(
    retrieveManaged := false,
    fork in Test := true,
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "scalatags" % "0.7.0"
    ),
    exportJars := true,
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    scalacOptions ++= Seq(
      "-target:jvm-1.8",
      "-deprecation",
      "-encoding",
      "UTF-8",
      "-unchecked",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-Xlint",
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

val playTemplates = project
  .in(file("."))
  .aggregate(vanilla, frontend, backend, native)
  .settings(commonSettings)

def gitHash: String =
  Try(Process("git rev-parse --short HEAD").lineStream.head).toOption.getOrElse("unknown")
