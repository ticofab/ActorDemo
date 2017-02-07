package circuitbreaker

import akka.actor.{Actor, Props}

/**
  * ActorDemo
  * Created by fabiotiriticco on 06/02/2017.
  */
case class BrewCoffee()

case class CoffeeBrewed()

case class OutOfCoffeeException() extends Exception

class CoffeeBrewer extends Actor {

  // internal state
  var caffeineReserve = 4

  override def receive = {
    case BrewCoffee =>

      if (caffeineReserve == 0) {
        println(self + ", out of coffee!")
        throw OutOfCoffeeException()
      }

      println(self.path.name + ", " + "brewing coffee")
      caffeineReserve -= 1
      sender ! CoffeeBrewed()
  }

  override def preRestart(reason: Throwable, message: Option[Any]) = {
    println(self.path.name + ", " + "starting up.. it will take me 20 seconds")
    super.preRestart(reason, message)
  }
}

object CoffeeBrewer {
  val props: Props = Props(new CoffeeBrewer)
}
