name := "Brighton Tide"

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

libraryDependencies ++= {
  Seq(
    "org.twitter4j" % "twitter4j-core" % "3.0.3",
    "joda-time" % "joda-time" % "1.6.2"
    )
}

parallelExecution in Test := false

// Customize any further dependencies as desired
libraryDependencies ++= Seq(
  "org.scalaz"        %% "scalaz-core"     % "6.0.4",
  "org.specs2"        %% "specs2"          % "1.14"              % "test",
  "junit"              % "junit"           % "4.7"              % "test",
  "ch.qos.logback"     % "logback-classic" % "1.0.6"            % "compile->default",
  "org.slf4j"          % "jcl-over-slf4j"  % "1.6.4"            % "test->default"// only used for debugging.
)

EclipseKeys.withSource := true

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

