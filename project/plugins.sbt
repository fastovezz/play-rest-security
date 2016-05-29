//resolvers += Resolver.url("sbt-plugins", url("http://dl.bintray.com/zalando/sbt-plugins"))(Resolver.ivyStylePatterns)
//
//resolvers += "zalando-bintray"  at "https://dl.bintray.com/zalando/maven"
//
//resolvers += "scalaz-bintray"   at "http://dl.bintray.com/scalaz/releases"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.3")

addSbtPlugin("com.typesafe.sbt" % "sbt-play-ebean" % "3.0.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.0")

//addSbtPlugin("de.zalando" % "sbt-play-swagger" % "0.1.9")
