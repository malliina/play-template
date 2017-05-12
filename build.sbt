import com.malliina.sbtplay.PlayProject

lazy val p = PlayProject.default("p")

val utilPlayDep = "com.malliina" %% "util-play" % "3.6.9"

organization := "com.malliina"
version := "0.0.1"
scalaVersion := "2.11.11"
resolvers ++= Seq(
  Resolver.jcenterRepo,
  Resolver.bintrayRepo("malliina", "maven")
)
libraryDependencies ++= Seq(
  utilPlayDep,
  utilPlayDep % Test classifier "tests"
)

pipelineStages := Seq(digest, gzip)
