import sbt._

object Dependency {
  private object Version {
    val ScalaLogging = "3.9.2"
    val Cats         = "1.6.0"
    val CatsEffect   = "1.2.0"
    val ScalaTest    = "3.0.5"
  }

  val Cats         = "org.typelevel"              %% "cats-core"     % Version.Cats
  val CatsEffect   = "org.typelevel"              %% "cats-effect"   % Version.CatsEffect
  val ScalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % Version.ScalaLogging
  val ScalaTest    = "org.scalatest"              %% "scalatest"     % Version.ScalaTest
}
