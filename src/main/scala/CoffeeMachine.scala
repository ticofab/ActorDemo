import akka.actor.{Actor, ActorLogging, ActorRef, Props}

/**
  * Created by fabiotiriticco on 30/06/2016.
  */

// these are the messages that can be exchanged
case object GiveMeNicotine

case class HereIsYourCoffee(numberOfCups: Int)

case class OutOfCoffeeException(unsatisfiedCustomer: ActorRef) extends Exception
// ------------------------

class CoffeeMachine extends Actor with ActorLogging {

  // internal state
  var nicotineReserve = 4

  log.info(self.path.name + ", started. nicotineReserve: " + nicotineReserve)

  // behaviour
  override def receive: Receive = {

    case GiveMeNicotine =>
      log.info(self.path.name + ", received GiveMeNicotine")
      if (nicotineReserve == 0) {
        log.info(self.path.name + ", out of coffee!")
        throw OutOfCoffeeException(sender)
      }

      // send nicotine back to our sender
      nicotineReserve = nicotineReserve - 1
      sender ! HereIsYourCoffee(1)

    case _ => log.info("unknown message")

  }

  override def postRestart(reason: Throwable) = {
    log.info(self.path.name + ", ...restart completed, my nicotineAmount is: " + nicotineReserve)

    // do we have any unsatisfied customers in the pipeline?
    reason match {
      case OutOfCoffeeException(unsatisfiedCustomer) =>
        nicotineReserve = nicotineReserve -1
        unsatisfiedCustomer ! HereIsYourCoffee(1)
    }

    super.postRestart(reason)
  }

}

object CoffeeMachine {
  def props = Props[CoffeeMachine]
}
