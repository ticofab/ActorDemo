name := "ActorDemo"

version := "1.0"

scalaVersion := "2.12.3"

libraryDependencies ++= {

  val akkaVersion = "2.5.3"

  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,

    // test
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )

}
