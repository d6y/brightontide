name := "brighton-tide-social-media"

scalaVersion := "2.12.6"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

val cirisVersion = "0.10.2"

libraryDependencies ++= Seq(
  "org.twitter4j" % "twitter4j-core" % "4.0.7",
  "org.typelevel" %% "cats-core" % "1.2.0",
  "com.softwaremill.sttp" %% "core" % "1.3.0",
  "is.cir" %% "ciris-cats" % cirisVersion,
  "is.cir" %% "ciris-core" % cirisVersion,
)

parallelExecution in Test := false

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.8.9" % "test",
  "junit" % "junit" % "4.7" % "test",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "compile->default",
  "org.slf4j" % "jcl-over-slf4j" % "1.7.25" % "test->default" // only used for debugging.
)
