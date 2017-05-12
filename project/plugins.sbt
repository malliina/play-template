scalaVersion := "2.10.6"

resolvers ++= Seq(
  Resolver.bintrayRepo("malliina", "maven"),
  ivyRepo("malliina bintray sbt", "https://dl.bintray.com/malliina/sbt-plugins/")
)

scalacOptions ++= Seq("-unchecked", "-deprecation")

addSbtPlugin("com.malliina" % "sbt-play" % "0.9.8")

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.0")

def ivyRepo(name: String, urlString: String) =
  Resolver.url(name, url(urlString))(Resolver.ivyStylePatterns)
