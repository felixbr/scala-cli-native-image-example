addCommandAlias("b", "show graalvm-native-image:packageBin")

lazy val root = (project in file("."))
  .settings(
    inThisBuild(
      List(
        scalaVersion := "2.12.8"
      )
    ),
    name := """scala-cli-native-image-example""",
    version := "0.1.0",
    libraryDependencies ++= List(
      Dependencies.decline,
      Dependencies.monix,
      Dependencies.betterFiles,
      Dependencies.scalaTest,
      Dependencies.scalaCheck
    ),
    graalVMNativeImageOptions ++= List(
      "--verbose",
      "-H:IncludeResources=.*" // Needed so resource files are bundled into the binary (see: https://github.com/oracle/graal/blob/master/substratevm/RESOURCES.md)
    )
  )
  .enablePlugins(GraalVMNativeImagePlugin)

scalacOptions ++= List( // useful compiler flags for scala
  "-deprecation",
  "-encoding",
  "UTF-8", // yes, this is 2 args
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint:_",
  "-Ywarn-unused:-imports",
  "-Yno-adapted-args",
  "-Ywarn-numeric-widen",
  "-Xfuture",
  "-Ypartial-unification"
)
