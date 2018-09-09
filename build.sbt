import com.malliina.sbtplay.PlayProject

lazy val p = PlayProject.default("p")

val utilPlayDep = "com.malliina" %% "util-play" % "4.14.0"

organization := "com.malliina"
version := "0.0.2"
scalaVersion := "2.12.6"
scalacOptions := Seq("-unchecked", "-deprecation")
resolvers ++= Seq(
  Resolver.jcenterRepo,
  Resolver.bintrayRepo("malliina", "maven")
)
libraryDependencies ++= Seq(
  utilPlayDep,
  utilPlayDep % Test classifier "tests"
)
dependencyOverrides ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.5.8",
  "com.typesafe.akka" %% "akka-actor" % "2.5.8"
)

pipelineStages := Seq(digest, gzip)
