import sbt._
import de.element34.sbteclipsify._

class Project(info: ProjectInfo) extends DefaultProject(info) with Eclipsify {

  val snapshots = ScalaToolsSnapshots
  val scalatoolsSnapshot = ScalaToolsSnapshots

  override def libraryDependencies = Set(
 	"joda-time" % "joda-time" % "1.6", 
    "net.databinder" %% "dispatch-twitter" % "0.7.8" % "compile->default",
	"org.scala-tools.testing" %% "specs" % "1.6.6" % "test->default"
  ) ++ super.libraryDependencies


}
