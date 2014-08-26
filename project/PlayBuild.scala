import com.typesafe.sbt.web.Import.Assets
import sbt.Keys._
import sbt._

object PlayBuild extends Build {

  lazy val p = Project("p", file(".")).enablePlugins(play.PlayScala).settings(commonSettings: _*)

  val commonSettings = Seq(
    scalaVersion := "2.11.2",
    retrieveManaged := false,
    fork in Test := true,
    resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.2.2" % "test",
      "org.scalatestplus" %% "play" % "1.1.0" % "test"
    ),
    // Play 2.3 no longer by default includes the public files (js, css, ...) in the main jar, but in a separate
    // *-assets.jar file created when you run 'dist'.
    // To include the assets in the main .jar when running packageBin, include the below lines.
    mappings in(Compile, packageBin) ++= {
      (unmanagedResourceDirectories in Assets).value flatMap
        (assetDir => (assetDir ***) pair relativeTo(baseDirectory.value))
    }
  )
}