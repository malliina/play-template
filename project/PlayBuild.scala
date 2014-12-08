import java.nio.file.Files

import com.mle.appbundler.AppBundler
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
  //  val volName = settingKey[String]("Volume name")
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
    exportJars := true,
    javacOptions ++= Seq("-source", "1.7", "-target", "1.7"),
    scalacOptions += "-target:jvm-1.7"
  ) ++ macConfig

  def macConfig = MacPlugin.macSettings ++ myMacConfSettings ++ Seq(
    jvmOptions ++= Seq("-Dhttp.port=8456"),
    launchdConf := Some(defaultLaunchd.value),
    dmg := {
      val pkgFile = pkg.value
      val buildFolder = pkgFile.getParent
      val destFile = buildFolder / s"${name.value}-${version.value}.dmg"
      val srcDir = buildFolder / "DmgContents"
      AppBundler.delete(srcDir)
      Files.createDirectories(srcDir)
//      val pkgDmgSourceFile =
      Files.move(pkgFile, srcDir / pkgFile.getFileName)
      val logger = streams.value
      val command = Seq(
        "/usr/bin/hdiutil",
        "create",
        "-volname",
        (displayName in Mac).value,
        "-srcfolder",
        srcDir.toString,
        "-ov",
        destFile.toString
      )
      ExeUtils.execute(command, logger)
//      logger.log info s"Created $destFile"
      destFile
    }
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

