import com.mle.sbt.azure.AzurePlugin
import com.mle.sbt.mac.MacPlugin
import com.mle.sbt.unix.LinuxPlugin
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

  val commonSettings = Seq(
    version := "0.0.1",
    scalaVersion := "2.11.4",
    retrieveManaged := false,
    fork in Test := true,
    updateOptions := updateOptions.value.withCachedResolution(true),
    libraryDependencies ++= Seq("com.github.malliina" %% "play-base" % "0.1.2")
  ) ++ nativePackagingSettings

  lazy val nativePackagingSettings = Seq(
    GenericKeys.pathMappings in MacPlugin.Mac := Seq.empty
  ) ++ SbtNativePackager.packagerSettings ++
    WinPlugin.windowsSettings ++
    LinuxPlugin.rpmSettings ++
    LinuxPlugin.debianSettings ++
    MacPlugin.macSettings ++
    GenericPlugin.confSettings ++
    AzurePlugin.azureSettings ++ Seq(
    WinKeys.upgradeGuid in Windows := "",
    mainClass := Some("com.mle.play.Starter"),
    linux.Keys.maintainer := "Michael Skogberg <malliina123@gmail.com>",
    // why conf?
    linux.Keys.packageSummary in Linux := "MusicPimp summary here.",
    rpm.Keys.rpmVendor := "Skogberg Labs",
    GenericKeys.manufacturer := "Skogberg Labs"
  )
}