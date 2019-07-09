val ScalatraVersion = "2.6.5"

organization := "fr.istic"

name := "ThomasDemo"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.6"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "org.scalatra" %% "scalatra-json" % ScalatraVersion,
  "org.json4s"   %% "json4s-jackson" % "3.6.6",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.9.v20180320" % "container;compile",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
 
)

enablePlugins(SbtTwirl)
enablePlugins(ScalatraPlugin)

unmanagedBase := baseDirectory.value / "src/main/scala/lib/"

unmanagedSourceDirectories in Compile += baseDirectory.value / "src/bank"

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
  case PathList("META-INF", xs @ _*) =>
      (xs) match {
        case ("MANIFEST.MF" :: Nil) =>
          MergeStrategy.discard
        case _ =>
          MergeStrategy.first
      }
  case PathList(xs @ _*)         => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

mainClass in assembly := Some("fr.istic.app.JettyLauncher")
