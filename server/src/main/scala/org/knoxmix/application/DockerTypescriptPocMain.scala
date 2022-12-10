package org.knoxmix.application

import org.knoxmix.infrastructure.config.DockerTypescriptPocConfig
import org.knoxmix.view.DockerReactPocServer

import scala.io.Source
import scala.util.Try

object DockerTypescriptPocMain extends App {

  val config = DockerTypescriptPocConfig.load(args)

  println(args.toList)

  List(
    "opt/ext_resources/public/webapps/react-app-1/asset-manifest.json"
    , "ext_resources/public/webapps/react-app-1/asset-manifest.json"
    , "public/webapps/react-app-1/asset-manifest.json"
    , "webapps/react-app-1/asset-manifest.json"
    , "react-app-1/asset-manifest.json"
  ).foreach{
    path =>
      println(s"$path ${Try(Source.fromResource(path).length).getOrElse(0)}")
  }



  new DockerReactPocServer(config.akkaConfig)
}
