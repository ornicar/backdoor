import sbt._
import Keys._
import play.Project._

trait Resolvers {
}

trait Dependencies {
}

object CharonBuild extends Build with Resolvers with Dependencies {

  val appName = "backdoor"
  val appVersion = "1.0"

  private val buildSettings = Project.defaultSettings ++ Seq(
    version := appVersion,
    resolvers := Seq(),
    libraryDependencies := Seq(),
    shellPrompt := {
      (state: State) ⇒ "%s> ".format(Project.extract(state).currentProject.id)
    },
    scalacOptions := Seq("-deprecation", "-unchecked")
  )

  lazy val backdoor = play.Project(appName, appVersion, settings = buildSettings)
}
