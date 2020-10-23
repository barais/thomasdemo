
organization := "fr.istic"

name := "ThomasDemo"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.13.3"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % "2.7.0",
  "org.scalatra" %% "scalatra-scalatest" % "2.7.0" % "test",
// https://mvnrepository.com/artifact/org.scalatra/scalatra-json
  "org.scalatra" %% "scalatra-json" % "2.7.0",
  "org.json4s"   %% "json4s-jackson" % "3.6.10",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.32.v20200930" % "container;compile",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
 
)

// enablePlugins(ScalatraPlugin)

unmanagedBase := baseDirectory.value / "src/libJars/"

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
