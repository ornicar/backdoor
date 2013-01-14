import sbt._
import Keys._
import PlayProject._

trait Resolvers {
}

trait Dependencies {
}

object CharonBuild extends Build with Resolvers with Dependencies {

  val appName = "backdoor"
  val appVersion = "0.1"

  private val buildSettings = Project.defaultSettings ++ Seq(
    version := appVersion,
    scalaVersion := "2.9.1",
    resolvers := Seq(),
    libraryDependencies := Seq(),
    shellPrompt := {
      (state: State) ⇒ "%s> ".format(Project.extract(state).currentProject.id)
    },
    scalacOptions := Seq("-deprecation", "-unchecked")
  )

  lazy val charon = PlayProject(appName, appVersion, mainLang = SCALA, settings = buildSettings)
}
