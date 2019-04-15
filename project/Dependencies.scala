import sbt._

object Dependencies {
  val monix       = "io.monix"             %% "monix"        % "3.0.0-RC2"
  val decline     = "com.monovore"         %% "decline"      % "0.5.0"
  val betterFiles = "com.github.pathikrit" %% "better-files" % "3.7.1"

  val scalaTest  = "org.scalatest"  %% "scalatest"  % "3.0.1"  % "test"
  val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
}
