package org.knoxmix.infrastructure.config

import com.typesafe.config.Config

case class AkkaConfig(host:String, port:Int)

object AkkaConfig {
  def load(config:Config):AkkaConfig = {
    AkkaConfig(
      host = config.getString("http.host"),
      port = config.getInt("http.port")
    )
  }
}