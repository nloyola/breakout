import scala.collection.immutable.Seq

lazy val lwjglVersion = "3.2.3"
lazy val jomlVersion  = "1.9.24"
lazy val imguiVersion = "1.76-0.9"
lazy val playVersion  = "2.8.1"

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
ThisBuild / organizationName := "scala-lwjgl"

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
      "io.imgui.java"               % "binding"         % imguiVersion,
      "io.imgui.java"               % "lwjgl3"          % imguiVersion,
      "io.imgui.java"               % s"natives-$os"    % imguiVersion,
      "com.typesafe.play"          %% "play-json"       % playVersion,
      "ch.qos.logback"              % "logback-classic" % "1.2.3",
      "org.scalatest"              %% "scalatest"       % "3.1.1" % Test
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
      "-target:jvm-1.8"
  )

javaOptions ++= {
  if (os == "macos")
    Seq("-XstartOnFirstThread")
  else
    //Seq("-Dorg.lwjgl.util.Debug=true", "-Dorg.lwjgl.util.DebugLoader=true")
    Seq("-Dorg.lwjgl.util.Debug=true")
}

fork in run := true
