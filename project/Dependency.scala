import sbt._

object Dependency {
  private object Version {
    val Cats1         = "1.6.0"
    val CatsEffect1   = "1.2.0"
    val ScalazSio1    = "1.0-RC4"
    val ScalaLogging3 = "3.9.2"
    val ScalaTest     = "3.0.5"
  }

  val Cats1         = "org.typelevel"              %% "cats-core"     % Version.Cats1
  val CatsEffect1   = "org.typelevel"              %% "cats-effect"   % Version.CatsEffect1
  val ScalazZio1    = "org.scalaz"                 %% "scalaz-zio"    % Version.ScalazSio1
  val ScalaLogging3 = "com.typesafe.scala-logging" %% "scala-logging" % Version.ScalaLogging3
  val ScalaTest     = "org.scalatest"              %% "scalatest"     % Version.ScalaTest
}
