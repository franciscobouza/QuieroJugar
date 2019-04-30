import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "ORT_TT_2018T1"
  val appVersion      = "1.0"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "mysql" % "mysql-connector-java" % "5.1.22",
    "org.json"%"org.json"%"chargebee-1.0"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
