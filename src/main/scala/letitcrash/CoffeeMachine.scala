package letitcrash

import akka.actor.{Actor, ActorRef, Props}

// these are the messages that can be exchanged
case class GiveMeCaffeine(sender: ActorRef)

case class HereIsYourCoffee(numberOfCups: Int)

case class OutOfCoffeeException() extends Exception

class CoffeeMachine extends Actor {

  // internal state
  var caffeineReserve = 4

  println(self.path.name + ", started. caffeine reserve: " + caffeineReserve)

  // behaviour
  override def receive: Receive = {

    case GiveMeCaffeine(user) =>
      println(self.path.name + ", received GiveMeCaffeine")
      if (caffeineReserve == 0) {
        println(self.path.name + ", out of coffee!")
        throw OutOfCoffeeException()
      }

      // send nicotine back to our sender
      caffeineReserve -= 1
      user ! HereIsYourCoffee(1)

  }

  override def preRestart(reason: Throwable, message: Option[Any]) {
    println(self.path.name + ", about to restart")
    super.preRestart(reason, message)
    message.foreach(self ! _)
  }

  override def postRestart(reason: Throwable) = {
    println(self.path.name + ", ...restart completed, my caffeine reserve is: " + caffeineReserve)
    super.postRestart(reason)
  }

}

object CoffeeMachine {
  def props = Props[CoffeeMachine]
}