scalaVersion := "2.12.8"

resolvers ++= Seq(
  Resolver.bintrayRepo("malliina", "maven"),
  ivyRepo("malliina bintray sbt", "https://dl.bintray.com/malliina/sbt-plugins/")
)

classpathTypes += "maven-plugin"

scalacOptions ++= Seq("-unchecked", "-deprecation")

// for vanilla
Seq(
  "com.malliina" % "sbt-play" % "1.4.1",
  "com.typesafe.sbt" % "sbt-digest" % "1.1.4",
  "com.typesafe.sbt" % "sbt-gzip" % "1.0.2",
  "com.eed3si9n" % "sbt-buildinfo" % "0.9.0"
) map addSbtPlugin

// for scala.js projects
Seq(
  "com.typesafe.sbt" % "sbt-less" % "1.1.2",
  "org.portable-scala" % "sbt-scalajs-crossproject" % "0.6.0",
  "org.scala-js" % "sbt-scalajs" % "0.6.26",
  "com.vmunier" % "sbt-web-scalajs" % "1.0.6"
) map addSbtPlugin

// for native
Seq(
  "com.malliina" %% "sbt-packager" % "2.6.0",
) map addSbtPlugin

dependencyOverrides ++= Seq(
  "org.webjars" % "webjars-locator-core" % "0.33",
  "org.codehaus.plexus" % "plexus-utils" % "3.0.17",
  "com.google.guava" % "guava" % "23.0"
)

def ivyRepo(name: String, urlString: String): URLRepository =
  Resolver.url(name, url(urlString))(Resolver.ivyStylePatterns)
