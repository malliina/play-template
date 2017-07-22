import com.malliina.sbtplay.PlayProject

lazy val p = PlayProject.default("p")

val utilPlayDep = "com.malliina" %% "util-play" % "4.1.0"

organization := "com.malliina"
version := "0.0.1"
scalaVersion := "2.12.2"
scalacOptions := Seq("-unchecked", "-deprecation")
resolvers ++= Seq(
  Resolver.jcenterRepo,
  Resolver.bintrayRepo("malliina", "maven")
)
libraryDependencies ++= Seq(
  utilPlayDep,
  utilPlayDep % Test classifier "tests"
)

pipelineStages := Seq(digest, gzip)
