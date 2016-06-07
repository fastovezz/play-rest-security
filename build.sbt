name := "play-rest-security"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean/*, PlaySwagger*/)/*.dependsOn(swagger)
lazy val swagger = RootProject(uri("ssh://git@github.com/CreditCardsCom/swagger-play.git"))*/

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaCore,
  javaJdbc,
  javaWs,
  evolutions,
  "org.postgresql" % "postgresql" % "9.4-1202-jdbc42",
  "com.typesafe.play" % "play_2.11" % "2.5.3",
  "com.typesafe.play" % "play-datacommons_2.11" % "2.5.3",

  "org.webjars.bower" % "angularjs" % "1.5.6",
  "org.webjars.bower" % "angular-route" % "1.5.6",

  "org.webjars" % "requirejs" % "2.2.0",
  "org.webjars" % "jquery" % "2.2.1",
  "org.webjars" % "bootstrap" % "3.3.6",
  "org.webjars" % "swagger-ui" % "2.1.8-M1",

  //todo: wait for play 2.5.x support
  // meanwhile using local dependency $DEV_HOME/projects/play/swagger-play25
  "io.swagger" %% "swagger-play2" % "1.5.2-SNAPSHOT"
)

//resolvers ++= Seq(
//  "zalando-bintray"  at "https://dl.bintray.com/zalando/maven",
//  "scalaz-bintray"   at "http://dl.bintray.com/scalaz/releases",
//  "jeffmay" at "https://dl.bintray.com/jeffmay/maven",
//  Resolver.url("sbt-plugins", url("http://dl.bintray.com/zalando/sbt-plugins"))(Resolver.ivyStylePatterns)
//)

routesGenerator := InjectedRoutesGenerator

//pipelineStages := Seq(rjs, digest, gzip)
