import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "Ingredienty"
  val appVersion      = "1.0-SNAPSHOT"
  
  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm
    // Dependencies of mongoDb
    //"se.radley" %% "play-plugins-salat" % "1.2"
    //"org.reactivemongo" %% "reactivemongo" % "0.8"
  )

  val aamain = play.Project(
    appName, appVersion, appDependencies, path = file("main")
  ).dependsOn(
    website, adminArea, common
  ).aggregate(
    website, adminArea, common
  )

  lazy val common = play.Project(
    appName + "-common", appVersion, appDependencies, path = file("common"))

  lazy val website = play.Project(
    appName + "-website", appVersion, appDependencies, path = file("website")
  ).dependsOn(common)

  lazy val adminArea = play.Project(
    appName + "-admin", appVersion, appDependencies, path = file("admin")
  ).dependsOn(common)

  

//  val dbLoader = Project("dbLoader", file("dbLoader")).settings(scalaVersion := "2.10.0")
}

