import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.7",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "http4stracer",
    libraryDependencies += scalaTest % Test
  )

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6")

scalacOptions ++= Seq(
  "-deprecation"
  , "-unchecked"
  , "-encoding", "UTF-8"
  , "-Xlint"
  , "-Xverify"
  , "-feature"
  , "-Ypartial-unification"
  , "-Xfatal-warnings"
  , "-language:_"
  , "-language:higherKinds"
  //,"-optimise"
)

val CatsVersion = "1.4.0"
val CatsEffectVersion = "1.0.0"
val CirceVersion = "0.10.1"
val Http4sVersion = "0.20.0-M3"
val Http4sTracerVersion = "1.0-RC5"

libraryDependencies ++= Seq(
  // Cats
  "org.typelevel"              %% "cats-core"            % CatsVersion,
  "org.typelevel"              %% "cats-effect"          % CatsEffectVersion,
  // json/circe                
  "io.circe"                   %% "circe-core"           % CirceVersion,
  "io.circe"                   %% "circe-generic"        % CirceVersion,
  "io.circe"                   %% "circe-generic-extras" % CirceVersion,
  // http4s                    
  "org.http4s"                 %% "http4s-core"          % Http4sVersion,
  "org.http4s"                 %% "http4s-circe"         % Http4sVersion,
  "com.github.gvolpe"          %% "http4s-tracer"        % Http4sTracerVersion,
  // Logging                   
  "ch.qos.logback"             %  "logback-classic"      % "1.1.3",
  "com.typesafe.scala-logging" %% "scala-logging"        % "3.9.0"
)