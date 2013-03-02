import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "blog"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "jp.t2v" %% "play21.auth" % "0.7",
    "org.mindrot" % "jbcrypt" % "0.3m",
    "postgresql" % "postgresql" % "8.4-702.jdbc4",
    "net.tanesha.recaptcha4j" % "recaptcha4j" % "0.0.7"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
  )

}
