import akka.actor.{Actor, ActorLogging, ActorRef, Props}

/**
  * Created by fabiotiriticco on 30/06/2016.
  */

// these are the messages that can be exchanged
case class GiveMeCaffeine(sender: ActorRef)

case class HereIsYourCoffee(numberOfCups: Int)

case class OutOfCoffeeException() extends Exception

// ------------------------

class CoffeeMachine extends Actor with ActorLogging {

  // internal state
  var caffeineReserve = 4

  log.info(self.path.name + ", started. caffeine reserve: " + caffeineReserve)

  // behaviour
  override def receive: Receive = {

    case GiveMeCaffeine(user) =>
      log.info(self.path.name + ", received GiveMeCaffeine")
      if (caffeineReserve == 0) {
        log.info(self.path.name + ", out of coffee!")
        throw OutOfCoffeeException()
      }

      // send nicotine back to our sender
      caffeineReserve -= 1
      user ! HereIsYourCoffee(1)

  }

  override def preRestart(reason: Throwable, message: Option[Any]) {
    log.info("about to restart")
    super.preRestart(reason, message)
    message.foreach(self ! _)
  }

  override def postRestart(reason: Throwable) = {
    log.info(self.path.name + ", ...restart completed, my caffeine reserve is: " + caffeineReserve)
    super.postRestart(reason)
  }

}

object CoffeeMachine {
  def props = Props[CoffeeMachine]
}
