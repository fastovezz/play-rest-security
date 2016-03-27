lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

name := "play-rest-security"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  javaWs,
  evolutions,
  "org.postgresql" % "postgresql" % "9.4-1202-jdbc42",
  "com.typesafe.play" % "play_2.11" % "2.5.0",
  "com.typesafe.play" % "play-datacommons_2.11" % "2.5.0",
  "org.webjars" % "jquery" % "2.2.1",
  "org.webjars" % "angularjs" % "1.3.0-beta.2",
  "org.webjars" % "requirejs" % "2.1.11-1",
  "org.webjars" % "bootstrap" % "3.0.1"
)

routesGenerator := InjectedRoutesGenerator

//pipelineStages := Seq(rjs, digest, gzip)
