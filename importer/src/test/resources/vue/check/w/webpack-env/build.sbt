organization := "com.olvind.scalablytyped"
name := "webpack-env"
version := "1.13-e0f5c5"
scalaVersion := "2.12.6"
enablePlugins(ScalaJSPlugin)
libraryDependencies ++= Seq(
  "com.olvind" %%% "runtime" % "1.0.0-M1",
  "com.olvind.scalablytyped" %%% "node" % "0.0-unknown-056634",
  "com.olvind.scalablytyped" %%% "std" % "0.0-unknown-b43f41",
  "org.scala-js" %%% "scalajs-dom" % "0.9.5")
publishArtifact in packageDoc := false
scalacOptions += "-P:scalajs:sjsDefinedByDefault"
        