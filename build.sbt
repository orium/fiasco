import scala.util.Properties

val Version = "0.1.0-SNAPSHOT"
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

lazy val fiascoCats1 =
  (project in file("fiasco-cats-1"))
    .settings(commonSettings: _*)
    .settings(
      name := "fiasco-cats-1",

      libraryDependencies ++= Seq(
        Dependency.Cats1 % Provided
      )
    )
    .dependsOn(fiascoCore % Provided)

lazy val fiascoCatsEffect1 =
  (project in file("fiasco-cats-effect-1"))
    .settings(commonSettings: _*)
    .settings(
      name := "fiasco-cats-effect-1",

      libraryDependencies ++= Seq(
        Dependency.CatsEffect1 % Provided
      )
    )
    .dependsOn(fiascoCore % Provided)

lazy val fiascoScalaz7 =
  (project in file("fiasco-scalaz-7"))
    .settings(commonSettings: _*)
    .settings(
      name := "fiasco-scalaz-7",

      libraryDependencies ++= Seq(
        Dependency.Scalaz7 % Provided
      )
    )
    .dependsOn(fiascoCore % Provided)

lazy val fiascoScalazZio1 =
  (project in file("fiasco-scalaz-zio-1"))
    .settings(commonSettings: _*)
    .settings(
      name := "fiasco-scalaz-zio-1",

      libraryDependencies ++= Seq(
        Dependency.ScalazZio1 % Provided
      )
    )
    .dependsOn(fiascoCore % Provided)

lazy val fiascoScalaLogging3 =
  (project in file("fiasco-scala-logging-3"))
    .settings(commonSettings: _*)
    .settings(
      name := "fiasco-scala-logging-3",

      libraryDependencies ++= Seq(
        Dependency.ScalaLogging3 % Provided
      )
    )
    .dependsOn(fiascoCore % Provided)

lazy val fiasco =
  (project in file("."))
    .settings(commonSettings: _*)
    .aggregate(
      fiascoCore,
      fiascoCats1,
      fiascoCatsEffect1,
      fiascoScalaz7,
      fiascoScalazZio1,
      fiascoScalaLogging3
    )
