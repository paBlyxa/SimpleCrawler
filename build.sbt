ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "Crawler"
  )

val http4sVersion = "0.23.4"

libraryDependencies ++= Seq(
    "org.typelevel"  %% "cats-effect"         % "3.2.9"
  , "org.http4s"     %% "http4s-blaze-server" % http4sVersion
  , "org.http4s"     %% "http4s-circe"        % http4sVersion
  , "org.http4s"     %% "http4s-core"         % http4sVersion
  , "org.http4s"     %% "http4s-dsl"          % http4sVersion
  , "org.http4s"     %% "http4s-server"       % http4sVersion
  , "org.http4s"     %% "http4s-blaze-client" % http4sVersion
  , "org.http4s"     %% "http4s-client"       % http4sVersion
  , "org.http4s"     %% "http4s-circe"        % http4sVersion
  , "io.circe"       %% "circe-generic"       % "0.14.1"
  , "ch.qos.logback"  % "logback-classic"     % "1.2.10"
  , "org.slf4j"       % "slf4j-api"           % "1.7.32"
  , "org.typelevel"  %% "log4cats-core"       % "2.1.1"
  , "org.typelevel"  %% "log4cats-slf4j"      % "2.1.1"
  , "org.scalatest"  %% "scalatest"           % "3.1.2"  % "test"
  , "org.scalacheck" %% "scalacheck"          % "1.14.3" % "test"
  , "co.fs2"         %% "fs2-core"            % "3.2.5"
)
