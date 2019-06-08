import scala.util.Properties

val Version = "0.1.0-SNAPSHOT"
val ScalaVersion = "2.12.8"

lazy val commonSettings = Seq(
  scalaVersion := ScalaVersion,

  organization := "fiasco",
  version := Version,

  homepage := Some(url("https://github.com/orium/fiasco/")),
  licenses := List("MPL-2.0" -> url("https://www.mozilla.org/en-US/MPL/2.0/")),

  scmInfo := Some(ScmInfo(
    url("https://github.com/orium/fiasco"),
    "scm:git@github.com:orium/fiasco.git"
  )),

  developers := List(Developer(
    id    = "orium",
    name  = "Diogo Sousa",
    email = "diogogsousa@gmail.com",
    url   = url("https://github.com/orium")
  )),

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
  },

  resolvers += Resolver.bintrayRepo("orium", "maven")
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
        Dependency.Cats1 % Provided,

        Dependency.ScalaTest % Test
      )
    )
    .dependsOn(fiascoCore % Provided)

lazy val fiascoCatsEffect1 =
  (project in file("fiasco-cats-effect-1"))
    .settings(commonSettings: _*)
    .settings(
      name := "fiasco-cats-effect-1",

      libraryDependencies ++= Seq(
        Dependency.CatsEffect1 % Provided,

        Dependency.ScalaTest % Test
      )
    )
    .dependsOn(fiascoCore % Provided)

lazy val fiascoScalaz7 =
  (project in file("fiasco-scalaz-7"))
    .settings(commonSettings: _*)
    .settings(
      name := "fiasco-scalaz-7",

      libraryDependencies ++= Seq(
        Dependency.Scalaz7 % Provided,

        Dependency.ScalaTest % Test
      )
    )
    .dependsOn(fiascoCore % Provided)

lazy val fiascoZio1 =
  (project in file("fiasco-zio-1"))
    .settings(commonSettings: _*)
    .settings(
      name := "fiasco-zio-1",

      libraryDependencies ++= Seq(
        Dependency.Zio1 % Provided,

        Dependency.ScalaTest % Test
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
    .settings(
      skip in publish := true
    ).aggregate(
      fiascoCore,
      fiascoCats1,
      fiascoCatsEffect1,
      fiascoScalaz7,
      fiascoZio1,
      fiascoScalaLogging3
    )

addCommandAlias(
  "codecov",
  ";clean; coverage; test; coverageReport; coverageAggregate"
)
