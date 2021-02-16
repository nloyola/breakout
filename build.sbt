import scala.collection.immutable.Seq

lazy val lwjglVersion = "3.2.3"
lazy val jomlVersion  = "1.10.0"
lazy val playVersion  = "2.9.2"

lazy val os = Option(System.getProperty("os.name", ""))
  .map(_.substring(0, 3).toLowerCase) match {
  case Some("win") => "windows"
  case Some("mac") => "macos"
  case _           => "linux"
}

name := "breakout"

version := "0.1"

scalaVersion := Option(System.getProperty("scala.version")).getOrElse("2.13.4")

ThisBuild / scalaVersion     := "2.13.4"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "game"
ThisBuild / organizationName := "breakout"

resolvers += Resolver.jcenterRepo

libraryDependencies ++=
  Seq("com.typesafe.scala-logging" %% "scala-logging"   % "3.9.2",
      "org.lwjgl"                   % "lwjgl"           % lwjglVersion,
      "org.lwjgl"                   % "lwjgl-opengl"    % lwjglVersion,
      "org.lwjgl"                   % "lwjgl-glfw"      % lwjglVersion,
      "org.lwjgl"                   % "lwjgl-stb"       % lwjglVersion,
      "org.lwjgl"                   % "lwjgl-assimp"    % lwjglVersion,
      "org.lwjgl"                   % "lwjgl-nanovg"    % lwjglVersion,
      "org.lwjgl"                   % "lwjgl"           % lwjglVersion classifier s"natives-$os",
      "org.lwjgl"                   % "lwjgl-opengl"    % lwjglVersion classifier s"natives-$os",
      "org.lwjgl"                   % "lwjgl-glfw"      % lwjglVersion classifier s"natives-$os",
      "org.lwjgl"                   % "lwjgl-stb"       % lwjglVersion classifier s"natives-$os",
      "org.lwjgl"                   % "lwjgl-assimp"    % lwjglVersion classifier s"natives-$os",
      "org.lwjgl"                   % "lwjgl-nanovg"    % lwjglVersion classifier s"natives-$os",
      "org.joml"                    % "joml"            % jomlVersion,
      "com.typesafe.play"          %% "play-json"       % playVersion,
      "ch.qos.logback"              % "logback-classic" % "1.2.3",
      "org.scalatest"              %% "scalatest"       % "3.2.3" % Test
  )

scalacOptions ++=
  Seq("-deprecation",
      "-encoding",
      "UTF-8",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-unchecked",
      "-Xfatal-warnings",
      "-Xlint:_,-missing-interpolator",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Yrangepos",
      "-target:11"
  )

javaOptions ++= {
  if (os == "macos")
    Seq("-XstartOnFirstThread")
  else
    Seq(
      // "-Dorg.lwjgl.util.DebugLoader=true",
      "-Dorg.lwjgl.util.Debug=true",
      "-Dorg.lwjgl.system.allocator=system"
    )
}

fork in run := true
