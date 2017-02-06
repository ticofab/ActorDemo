package circuitbreaker

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
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

class CoffeeMachine(coffeeBrewer: ActorRef) extends Actor with ActorLogging {

  val breaker = CircuitBreaker(
    context.system.scheduler,
    1,
    10.second,
    20.second)
    .onOpen(log.info("Circuit Breaker is open"))
    .onClose(log.info("Circuit Breaker is closed"))
    .onHalfOpen(log.info("Circuit Breaker is half open, next message will go through"))

  // behaviour
  override def receive: Receive = {

    case GiveMeCaffeine(user, euros) =>
      log.info("received GiveMeCaffeine")
      if (euros != 2) user ! InvalidAmount(euros)
      else {
        // let's see if we can brew coffee
        val futureCoffee = breaker.withCircuitBreaker((coffeeBrewer ? BrewCoffee) (5.second))
        futureCoffee.mapTo[CoffeeBrewed].map(cb => {
          log.info("sending coffee to user")
          user ! HereIsYourCoffee(1)
        }).recover {

          case t: AskTimeoutException =>
            log.info("the coffee brewer timed out")
            user ! SorrySomethingIsWrong()

          case co: CircuitBreakerOpenException =>
            log.info("circuit breaker open")
            user ! OutOfOrder()

        }
      }

  }


}

object CoffeeMachine {
  def props(coffeeBrewer: ActorRef) = Props(new CoffeeMachine(coffeeBrewer))
}
