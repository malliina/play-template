scalaVersion := "2.10.6"

resolvers ++= Seq(
  ivyRepo("malliina bintray sbt", "https://dl.bintray.com/malliina/sbt-plugins/")
)

scalacOptions ++= Seq("-unchecked", "-deprecation")

addSbtPlugin("com.malliina" % "sbt-play" % "0.9.8")

def ivyRepo(name: String, urlString: String) =
  Resolver.url(name, url(urlString))(Resolver.ivyStylePatterns)
