package circuitbreaker

import akka.actor.{Actor, ActorLogging, Props}

/**
  * ActorDemo
  * Created by fabiotiriticco on 06/02/2017.
  */
case class BrewCoffee()

case class CoffeeBrewed()

case class OutOfCoffeeException() extends Exception

class CoffeeBrewer extends Actor with ActorLogging {

  // internal state
  var caffeineReserve = 4

  override def receive = {
    case BrewCoffee =>

      if (caffeineReserve == 0) {
        log.error("out of coffee!")
        throw OutOfCoffeeException()
      }

      log.info("brewing coffee")
      caffeineReserve -= 1
      sender ! CoffeeBrewed()
  }

  override def preRestart(reason: Throwable, message: Option[Any]) = {
    log.info("starting up.. it will take me 20 seconds")
    super.preRestart(reason, message)
  }
}

object CoffeeBrewer {
  val props: Props = Props(new CoffeeBrewer)
}
