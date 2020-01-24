scalaVersion := "2.12.10"

scalacOptions ++= Seq("-unchecked", "-deprecation")

// for vanilla
Seq(
  "com.typesafe.play" % "sbt-plugin" % "2.7.3",
  "com.typesafe.sbt" % "sbt-digest" % "1.1.4",
  "com.typesafe.sbt" % "sbt-gzip" % "1.0.2",
  "com.eed3si9n" % "sbt-buildinfo" % "0.9.0"
) map addSbtPlugin

// for scala.js projects
Seq(
  "com.typesafe.sbt" % "sbt-less" % "1.1.2",
  "org.portable-scala" % "sbt-scalajs-crossproject" % "0.6.1",
  "org.scala-js" % "sbt-scalajs" % "0.6.31",
  "com.vmunier" % "sbt-web-scalajs" % "1.0.6",
  "ch.epfl.scala" % "sbt-web-scalajs-bundler" % "0.14.0",
  "ch.epfl.scala" % "sbt-bloop" % "1.3.4",
  "org.scalameta" % "sbt-scalafmt" % "2.2.1"
) map addSbtPlugin

// for native
Seq(
  "com.malliina" % "sbt-packager" % "2.8.4"
) map addSbtPlugin
