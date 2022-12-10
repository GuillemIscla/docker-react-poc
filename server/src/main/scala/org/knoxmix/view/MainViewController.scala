package org.knoxmix.view

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.knoxmix.application.CountGreetingsApplication

class MainViewController(countGreetingsApplication: CountGreetingsApplication) {

    val routes: Route =
      path("message" / IntNumber){ int =>
        val times = countGreetingsApplication.getAndAddUp(int)
        complete(s"Hello ${if(times>0) s"(again x $times) " else ""}react-app-$int. This is the server.")
      }
}
