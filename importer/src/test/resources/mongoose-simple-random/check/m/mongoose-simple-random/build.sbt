organization := "com.olvind.scalablytyped"
name := "mongoose-simple-random"
version := "0.4-acf2a1"
scalaVersion := "2.12.6"
enablePlugins(ScalaJSPlugin)
libraryDependencies ++= Seq(
  "com.olvind" %%% "runtime" % "1.0.0-M1",
  "com.olvind.scalablytyped" %%% "mongoose" % "0.0-unknown-925484",
  "com.olvind.scalablytyped" %%% "node" % "0.0-unknown-336112",
  "com.olvind.scalablytyped" %%% "std" % "0.0-unknown-524c20",
  "org.scala-js" %%% "scalajs-dom" % "0.9.5")
publishArtifact in packageDoc := false
scalacOptions += "-P:scalajs:sjsDefinedByDefault"
        