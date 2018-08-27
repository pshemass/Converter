organization := "com.olvind.scalablytyped"
name := "material-ui"
version := "0.0-unknown-59f62a"
scalaVersion := "2.12.6"
enablePlugins(ScalaJSPlugin)
libraryDependencies ++= Seq(
  "com.olvind" %%% "runtime" % "1.0.0-M1",
  "com.olvind.scalablytyped" %%% "react" % "0.0-unknown-81c9e3",
  "com.olvind.scalablytyped" %%% "std" % "0.0-unknown-4c5d35",
  "org.scala-js" %%% "scalajs-dom" % "0.9.5")
publishArtifact in packageDoc := false
scalacOptions += "-P:scalajs:sjsDefinedByDefault"
        