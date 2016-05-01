organization := "com.github.dlisin"
name := "akka-persistence-hazelcast"
version := "0.0.1"

scalaVersion := "2.11.8"

resolvers ++= {
  Seq(
    "nilskp/maven on bintray" at "http://dl.bintray.com/nilskp/maven"
  )
}

libraryDependencies ++= {
  val akkaVersion = "2.4.4"
  val hazelcastVersion = "3.6.2"

  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-persistence-tck" % akkaVersion % Test,

    "com.hazelcast" % "hazelcast" % hazelcastVersion % Provided,

    "org.scalatest" %% "scalatest" % "2.2.6" % Test
  )
}
