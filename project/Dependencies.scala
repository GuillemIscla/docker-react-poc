import sbt._

object Dependencies {

  object ver {
    val akka = "2.6.19"
    val akkaHttp = "10.2.9"
  }

  lazy val akkaHttp: Seq[ModuleID] =
    Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % ver.akka
      , "com.typesafe.akka" %% "akka-stream" % ver.akka
      , "com.typesafe.akka" %% "akka-http" % ver.akkaHttp
      , "com.typesafe.akka" %% "akka-http-spray-json" % ver.akkaHttp
      , "com.typesafe.akka" %% "akka-stream" % ver.akka
    )


}
