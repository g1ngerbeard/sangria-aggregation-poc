name := "sangria-aggregation-poc"

version := "1.0"

scalaVersion := "2.12.1"

val sangriaVersion = "1.3.0"
//val sangriaCirceVersion = "1.1.0"
val sangriaSprayJsonVersion = "1.0.0"
val akkaHttpVersion = "10.0.10"

val circeVersion                  = "0.8.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "org.sangria-graphql" %% "sangria" % sangriaVersion,
//  "org.sangria-graphql" %% "sangria-circe" % sangriaCirceVersion,
  "org.sangria-graphql" %% "sangria-spray-json" % sangriaSprayJsonVersion
//  "io.circe"            %% "circe-core"        % circeVersion,
//  "io.circe"            %% "circe-generic"     % circeVersion,
//  "io.circe"            %% "circe-parser"      % circeVersion
)
