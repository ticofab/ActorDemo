package circuitbreaker

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.{AskTimeoutException, CircuitBreaker, CircuitBreakerOpenException, ask}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * ActorDemo
  * Created by fabiotiriticco on 06/02/2017.
  */
// these are the messages that can be exchanged
case class GiveMeCaffeine(sender: ActorRef, euros: Int)

case class HereIsYourCoffee(numberOfCups: Int)

case class InvalidAmount(euros: Int)

case class SorrySomethingIsWrong()

case class OutOfOrder()

class CoffeeMachine(coffeeBrewer: ActorRef) extends Actor {

  val breaker = CircuitBreaker(
    context.system.scheduler,
    1,
    10.second,
    20.second)
    .onOpen(println(self.path.name + ", " + "Circuit Breaker is open"))
    .onClose(println(self.path.name + ", " + "Circuit Breaker is closed"))
    .onHalfOpen(println(self.path.name + ", " + "Circuit Breaker is half open, next message will go through"))

  // behaviour
  override def receive: Receive = {

    case GiveMeCaffeine(user, euros) =>
      if (euros != 2) user ! InvalidAmount(euros)
      else {
        // let's see if we can brew coffee
        val futureCoffee = breaker.withCircuitBreaker((coffeeBrewer ? BrewCoffee) (8.second))
        futureCoffee.mapTo[CoffeeBrewed].map(cb => {
          user ! HereIsYourCoffee(1)
        }).recover {

          case t: AskTimeoutException =>
            println(self.path.name + ", " + "the coffee brewer timed out")
            user ! SorrySomethingIsWrong()

          case co: CircuitBreakerOpenException =>
            println(self.path.name + ", " + "circuit breaker open")
            user ! OutOfOrder()

        }
      }

  }


}

object CoffeeMachine {
  def props(coffeeBrewer: ActorRef) = Props(new CoffeeMachine(coffeeBrewer))
}
