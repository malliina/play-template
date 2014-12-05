import java.nio.file.{StandardCopyOption, Files, Path}

import com.mle.appbundler.AppBundler
import com.mle.file.StorageFile
import com.mle.sbt.GenericKeys._
import com.mle.sbt.azure.AzurePlugin
import com.mle.sbt.mac.MacKeys._
import com.mle.sbt.mac.MacPlugin
import com.mle.sbt.mac.MacPlugin._
import com.mle.sbt.unix.UnixKeys._
import com.mle.sbt.unix.{LinuxPlugin, UnixKeys}
import com.mle.sbt.win.{WinKeys, WinPlugin}
import com.mle.sbt.{GenericKeys, GenericPlugin}
import com.mle.sbtplay.PlayProjects
import com.typesafe.sbt.SbtNativePackager
import com.typesafe.sbt.SbtNativePackager._
import com.typesafe.sbt.packager.{linux, rpm}
import sbt.Keys._
import sbt._

object PlayBuild extends Build {

  lazy val p = PlayProjects.plainPlayProject("p").settings(commonSettings: _*)

  lazy val commonSettings = Seq(
    organization := "com.github.malliina",
    version := "0.0.13",
    scalaVersion := "2.11.4",
    retrieveManaged := false,
    fork in Test := true,
    updateOptions := updateOptions.value.withCachedResolution(true),
    libraryDependencies ++= Seq("com.github.malliina" %% "play-base" % "0.1.2")
  ) ++ nativePackagingSettings

  val macPkgRoot = settingKey[Path]("Root folder for OSX packaging")
  val macAppDir = settingKey[Path]("DisplayName.app dir")
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
    macSettings ++
    GenericPlugin.confSettings ++
    AzurePlugin.azureSettings ++ Seq(
    WinKeys.upgradeGuid in Windows := "",
    mainClass := Some("com.mle.play.Starter"),
    linux.Keys.maintainer := "Michael Skogberg <malliina123@gmail.com>",
    // why conf?
    linux.Keys.packageSummary in Linux := "MusicPimp summary here.",
    rpm.Keys.rpmVendor := "Skogberg Labs",
    GenericKeys.manufacturer := "Skogberg Labs",
    exportJars := true,
    javacOptions ++= Seq("-source", "1.7", "-target", "1.7"),
    scalacOptions += "-target:jvm-1.7"
  ) ++ myMacConfSettings ++ MacPlugin.macSettings ++ myMacSettings

  def myMacSettings = Seq(
    jvmOptions := Seq("-Dhttp.port=8456"),
    hideDock := true,
    displayName := "P",
    macPkgRoot := (targetPath in Mac).value / "out",
    macAppTarget := macPkgRoot.value / "Applications",
    macAppDir := macAppTarget.value / s"${displayName.value}.app",
    macContentsDir := macAppDir.value / "Contents",
    macDistribution := (targetPath in Mac).value / "Distribution.xml",
    macResources := (targetPath in Mac).value / "Resources",
    macScripts := (targetPath in Mac).value / "Scripts",
    macPkgDir := (targetPath in Mac).value / "Pkg",
    macPackagePath := ((targetPath in Mac).value / s"${name.value}-${version.value}.pkg"),
    macPkgBuild := Seq(
      "/usr/bin/pkgbuild",
      "--root",
      macPkgRoot.value.toString,
      "--identifier",
      appIdentifier.value,
      "--version",
      version.value,
      "--scripts",
      macScripts.value.toString,
      "--ownership",
      "recommended",
      (macPkgDir.value / s"${name.value}.pkg").toString),
    macProductBuild := Seq(
      "/usr/bin/productbuild",
      "--distribution",
      macDistribution.value.toString,
      "--resources",
      macResources.value.toString,
      "--version",
      version.value,
      "--package-path",
      macPkgDir.value.toString,
      macPackagePath.value.toString),
    macPackage := {
      Files.createDirectories(macPkgRoot.value)
      val pkgHome = (GenericKeys.pkgHome in Mac).value
      copy(pkgHome / "Distribution.xml", (targetPath in Mac).value)
      copy(pkgHome / "welcome.html", macResources.value)
      copy(pkgHome / "license.html", macResources.value)
      copy(pkgHome / "conclusion.html", macResources.value)
      Files.createDirectories(macScripts.value)
      copyExecutable(pkgHome / "preinstall", macScripts.value)
      copyExecutable(pkgHome / "postinstall", macScripts.value)
      copy(pkgHome / s"${organization.value}.plist", macPkgRoot.value / "Library" / "LaunchDaemons")
      app.value
      val logger = streams.value
//      val bundle = macAppDir.value
//      val cmd = Seq("/usr/bin/SetFile", "-a", "B", bundle.toString)
//      ExeUtils.execute(cmd, streams.value)
      Files.createDirectories(macPkgDir.value)
      ExeUtils.execute(macPkgBuild.value, logger)
      ExeUtils.execute(macProductBuild.value, logger)
      val outFile = macPackagePath.value

      /**
       * If the out directory used to build the .pkg is not deleted, the app will fail to install properly on the
       * development machine. I don't know why, I suspect I'm doing something wrong, but deleting the directory is a
       * workaround.
       */
      AppBundler.delete(macPkgRoot.value)
      logger.log info s"Created $outFile"
      outFile
    }
  )

  def copy(src: Path, destDir: Path): Path = {
    Files.createDirectories(destDir)
    Files.copy(src, destDir / src.getFileName, StandardCopyOption.REPLACE_EXISTING)
  }
  def copyExecutable(src:Path, destDir:Path): Path = {
    val result = copy(src,destDir)
    result.toFile.setExecutable(true, false)
    result
  }

  def myMacConfSettings = inConfig(Mac)(Seq(
    GenericKeys.appIcon := Some(GenericKeys.pkgHome.value / "guitar.icns"),
    pathMappings ++= {
      val home = GenericKeys.pkgHome.value
      def map(name: String, destDir: Path) = (home / name) -> destDir / name

      Seq(
        map("Distribution.xml", (targetPath in Mac).value),
        map("welcome.html", macResources.value),
        map("license.html", macResources.value),
        map("conclusion.html", macResources.value),
//        map("postinstall", macScripts.value),
//        map("preinstall", macScripts.value),
        plistFile.value -> macPkgRoot.value / "Library" / "LaunchDaemons" / plistFile.value.getFileName
      )
    },
    libMappings := libMappings.value.map(pair => {
      val (src, dest) = pair
      src -> macContentsDir.value / "Resources" / "Java" / dest.getFileName
    }),
    pathMappings := pathMappings.value.map(pair => {
      val (src, dest) = pair
      val prefix = UnixKeys.unixHome.value.toString
      val destString = dest.toString
      val suffix =
        if (destString startsWith prefix) macContentsDir.value / destString.drop(prefix.length + 1)
        else dest
      val localDest = targetPath.value / suffix
      src -> localDest
    })
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

