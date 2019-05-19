import sbt._

object Plugin {
  private object Version {
    val SbtRelease   = "1.0.9"
    val SbtUpdates   = "0.3.1"
    val SbtScoverage = "1.5.1"
  }

  val SbtRelease   = "com.github.gseitz" %  "sbt-release"   % Version.SbtRelease
  val SbtUpdates   = "com.timushev.sbt"  %% "sbt-updates"   % Version.SbtUpdates
  val SbtScoverage = "org.scoverage"     %  "sbt-scoverage" % Version.SbtScoverage
}
