organization := "com.github.dlisin"

name := "akka-persistence-hazelcast"

version := "0.0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= {
  val akkaVersion = "2.4.0-RC3"
  val hazelcastVersion = "3.5.2"

  Seq(
    "com.typesafe.akka"  %% "akka-actor"            % akkaVersion,
    "com.typesafe.akka"  %% "akka-slf4j"            % akkaVersion,
    "com.typesafe.akka"  %% "akka-persistence"      % akkaVersion,
    "com.hazelcast"      %  "hazelcast"             % hazelcastVersion,
    "com.typesafe.akka"  %% "akka-testkit"          % akkaVersion     % Test,
    "com.typesafe.akka"  %% "akka-persistence-tck"  % akkaVersion     % Test,
    "org.scalatest"      %% "scalatest"             % "2.2.5"         % Test
  )
}
