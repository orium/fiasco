import scala.util.Properties

val Version = "0.0.0" // WIP! use sbt release
val ScalaVersion = "2.12.8"

lazy val commonSettings = Seq(
  scalaVersion := ScalaVersion,

  organization := "fiasco",
  version := Version,

  scalacOptions ++= {
    Seq(
      "-deprecation",
      "-feature",
      "-unchecked"
    ) ++ {
      Properties.envOrNone("CI").isDefined match {
        case true  => Seq("-Xfatal-warnings")
        case false => Seq.empty
      }
    }
  }
)

lazy val fiascoCore =
  (project in file("fiasco-core"))
    .settings(commonSettings: _*)
    .settings(
      name := "fiasco-core",

      libraryDependencies ++= Seq(
        Dependency.ScalaTest % Test
      )
    )

lazy val fiascoLogging3 =
  (project in file("fiasco-logging-3"))
    .settings(commonSettings: _*)
    .settings(
      name := "fiasco-logging-3",

      libraryDependencies ++= Seq(
        Dependency.ScalaLogging % Provided
      )
    )
    .dependsOn(fiascoCore % Provided)

lazy val fiascoCats1 =
  (project in file("fiasco-cats-1"))
    .settings(commonSettings: _*)
    .settings(
      name := "fiasco-cats-1",

      libraryDependencies ++= Seq(
        Dependency.Cats % Provided
      )
    )
    .dependsOn(fiascoCore % Provided)

lazy val fiascoCatsEffect1 =
  (project in file("fiasco-cats-effect-1"))
    .settings(commonSettings: _*)
    .settings(
      name := "fiasco-cats-effect-1",

      libraryDependencies ++= Seq(
        Dependency.CatsEffect % Provided
      )
    )
    .dependsOn(fiascoCore % Provided)

lazy val fiasco =
  (project in file("."))
    .settings(commonSettings: _*)
    .aggregate(
      fiascoCore,
      fiascoLogging3,
      fiascoCats1,
      fiascoCatsEffect1
    )
