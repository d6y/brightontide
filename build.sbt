name := "Brighton Tide"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

libraryDependencies ++= {
  Seq(
    "org.twitter4j" % "twitter4j-core" % "3.0.5",
    "joda-time" % "joda-time" % "2.3",
    "org.joda" % "joda-convert" % "1.2"
    )
}

parallelExecution in Test := false

// Customize any further dependencies as desired
libraryDependencies ++= Seq(
  "org.scalaz"        %% "scalaz-core"     % "7.0.5",
  "org.specs2"        %% "specs2"          % "2.3.7"              % "test",
  "junit"              % "junit"           % "4.7"              % "test",
  "ch.qos.logback"     % "logback-classic" % "1.0.13"            % "compile->default",
  "org.slf4j"          % "jcl-over-slf4j"  % "1.7.5"            % "test->default"// only used for debugging.
)

EclipseKeys.withSource := true

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

