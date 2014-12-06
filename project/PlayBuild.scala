import java.nio.file.Path

import com.mle.file.StorageFile
import com.mle.sbt.GenericKeys._
import com.mle.sbt.GenericPlugin
import com.mle.sbt.azure.AzurePlugin
import com.mle.sbt.mac.MacKeys._
import com.mle.sbt.mac.MacPlugin
import com.mle.sbt.mac.MacPlugin._
import com.mle.sbt.unix.LinuxPlugin
import com.mle.sbt.win.{WinKeys, WinPlugin}
import com.mle.sbtplay.PlayProjects
import com.typesafe.sbt.SbtNativePackager
import com.typesafe.sbt.SbtNativePackager._
import com.typesafe.sbt.packager.linux
import sbt.Keys._
import sbt._

object PlayBuild extends Build {

  lazy val p = PlayProjects.plainPlayProject("p").settings(commonSettings: _*)

  lazy val commonSettings = Seq(
    organization := "com.github.malliina",
    version := "0.2.1",
    scalaVersion := "2.11.4",
    retrieveManaged := false,
    fork in Test := true,
    updateOptions := updateOptions.value.withCachedResolution(true),
    libraryDependencies ++= Seq("com.github.malliina" %% "play-base" % "0.1.2")
  ) ++ nativePackagingSettings

  val macPkgRoot = settingKey[Path]("Root folder for OSX packaging")
  val macAppDir = settingKey[Path]("App.app dir")
  val macContentsDir = settingKey[Path]("Contents dir")
  val macResources = settingKey[Path]("HTML resources for OSX packaging")
  val macScripts = settingKey[Path]("Scripts for OSX packaging")
  val macPkgDir = settingKey[Path]("Temporary package-path for OSX")
  val macDistribution = settingKey[Path]("Distribution.xml for OSX")
  val macPkgBuild = taskKey[Seq[String]]("The /usr/bin/pkgbuild command to run")
  val macProductBuild = taskKey[Seq[String]]("The /usr/bin/productbuild command to run")
  val macPackagePath = settingKey[Path]("The path to the target .pkg for OSX")
  val macPackage = taskKey[Path]("Creates a .pkg for OSX")

  lazy val nativePackagingSettings = SbtNativePackager.packagerSettings ++
    WinPlugin.windowsSettings ++
    LinuxPlugin.rpmSettings ++
    LinuxPlugin.debianSettings ++
    GenericPlugin.confSettings ++
    AzurePlugin.azureSettings ++ Seq(
    WinKeys.upgradeGuid in Windows := "",
    mainClass := Some("com.mle.play.Starter"),
    linux.Keys.maintainer := "Michael Skogberg <malliina123@gmail.com>",
    // why conf?
    linux.Keys.packageSummary in Linux := "MusicPimp summary here.",
    manufacturer := "Skogberg Labs",
    launchdConf := Some(defaultLaunchd.value),
    exportJars := true,
    javacOptions ++= Seq("-source", "1.7", "-target", "1.7"),
    scalacOptions += "-target:jvm-1.7"
  ) ++ macConfig

  def macConfig = MacPlugin.macSettings ++ myMacConfSettings ++ Seq(
    jvmOptions ++= Seq("-Dhttp.port=8456"),
    deleteOutOnComplete := true,
    launchdConf := Some(defaultLaunchd.value)
  )
  def myMacConfSettings = MacPlugin.macSettings ++ inConfig(Mac)(Seq(
    appIcon := Some(pkgHome.value / "guitar.icns"),
    displayName := "P"
  ))
}

/**
 * @author Michael
 */
object ExeUtils {
  /**
   * Executes the supplied command with the given parameters,
   * logging the command and any subsequent output using the logger's INFO level.
   *
   * @param cmd command to execute
   * @param logger the logger
   */
  def execute(cmd: Seq[String], logger: TaskStreams) {
    val output = execute2(cmd, logger)
    output.foreach(line => logger.log.info(line))
  }

  /**
   * Executes the supplied command, logging only the command executed.
   *
   * @param cmd
   * @param logger
   * @return all output lines up to termination
   */
  def execute2(cmd: Seq[String], logger: TaskStreams): Stream[String] = {
    logger.log.info(cmd.mkString(" "))
    Process(cmd.head, cmd.tail).lines
  }
}

