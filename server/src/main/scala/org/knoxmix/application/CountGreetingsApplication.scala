package org.knoxmix.application

class CountGreetingsApplication() {
  var greetingCounter1:Int = 0
  var greetingCounter2:Int = 0

  def getAndAddUp(webappNum:Int):Int = {
    webappNum match {
      case 1 =>
        val toReturn = greetingCounter1
        greetingCounter1 += 1
        toReturn
      case 2 =>
        val toReturn = greetingCounter2
        greetingCounter2 += 1
        toReturn
      case _ => throw new Exception("Only counters 1 and 2 implemented")
    }
  }

}
