name := "Brighton Tide"

scalaVersion := "2.12.1"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

libraryDependencies ++= {
  Seq(
    "org.twitter4j" % "twitter4j-core" % "3.0.5",
    "joda-time" % "joda-time" % "2.3",
    "org.joda" % "joda-convert" % "1.2"
    )
}

parallelExecution in Test := false

libraryDependencies ++= Seq(
  "org.specs2"        %% "specs2-core"     % "3.8.9"              % "test",
  "junit"              % "junit"           % "4.7"              % "test",
  "ch.qos.logback"     % "logback-classic" % "1.0.13"            % "compile->default",
  "org.slf4j"          % "jcl-over-slf4j"  % "1.7.5"            % "test->default"// only used for debugging.
)
