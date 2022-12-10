package org.knoxmix.infrastructure.config

import com.typesafe.config.ConfigFactory

case class DockerTypescriptPocConfig(
  akkaConfig: AkkaConfig
)

object DockerTypescriptPocConfig {
  def load(args:Array[String]):DockerTypescriptPocConfig = {
    val mainConfig = ConfigFactory.load()
    DockerTypescriptPocConfig(
      akkaConfig = AkkaConfig.load(mainConfig.getConfig("akka"))
    )
  }
}