import sbt._

object Dependency {
  private object Version {
    val Cats1         = "1.6.1"
    val CatsEffect1   = "1.3.1"
    val Scalaz7       = "7.2.27"
    val Zio1          = "1.0.0-RC8"
    val ScalaLogging3 = "3.9.2"
    val ScalaTest     = "3.0.7"
  }

  val Cats1         = "org.typelevel"              %% "cats-core"     % Version.Cats1
  val CatsEffect1   = "org.typelevel"              %% "cats-effect"   % Version.CatsEffect1
  val Scalaz7       = "org.scalaz"                 %% "scalaz-core"   % Version.Scalaz7
  val Zio1          = "dev.zip"                    %% "core"          % Version.Zio1
  val ScalaLogging3 = "com.typesafe.scala-logging" %% "scala-logging" % Version.ScalaLogging3
  val ScalaTest     = "org.scalatest"              %% "scalatest"     % Version.ScalaTest
}
