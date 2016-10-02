import akka.actor.{Actor, ActorLogging, ActorRef, Props}

/**
  * Created by fabiotiriticco on 30/06/2016.
  */

// these are the messages that can be exchanged
case object GiveMeCaffeine

case class HereIsYourCoffee(numberOfCups: Int)

case class OutOfCoffeeException(unsatisfiedCustomer: ActorRef) extends Exception
// ------------------------

class CoffeeMachine extends Actor with ActorLogging {

  // internal state
  var caffeineReserve = 4

  log.info(self.path.name + ", started. caffeine reserve: " + caffeineReserve)

  // behaviour
  override def receive: Receive = {

    case GiveMeCaffeine =>
      log.info(self.path.name + ", received GiveMeNicotine")
      if (caffeineReserve == 0) {
        log.info(self.path.name + ", out of coffee!")
        throw OutOfCoffeeException(sender)
      }

      // send nicotine back to our sender
      caffeineReserve = caffeineReserve - 1
      sender ! HereIsYourCoffee(1)

    case _ => log.info("unknown message")

  }

  override def postRestart(reason: Throwable) = {
    log.info(self.path.name + ", ...restart completed, my caffeine reserve is: " + caffeineReserve)

    // do we have any unsatisfied customers in the pipeline?
    reason match {
      case OutOfCoffeeException(unsatisfiedCustomer) =>
        caffeineReserve = caffeineReserve -1
        unsatisfiedCustomer ! HereIsYourCoffee(1)
    }

    super.postRestart(reason)
  }

}

object CoffeeMachine {
  def props = Props[CoffeeMachine]
}
