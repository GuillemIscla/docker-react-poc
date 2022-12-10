package org.knoxmix.view

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.io.Source

class WebappViewController() {

  val routes: Route =
    pathPrefix("webapp") {
      path("react-app-1") {
        get {
          val html = Source.fromResource("public/webapps/react-app-1/index.html").mkString
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, html))
        }
      } ~
      path("react-app-2") {
        get {
          val html = Source.fromResource("public/webapps/react-app-2/index.html").mkString
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, html))
        }
      }
    }

}
