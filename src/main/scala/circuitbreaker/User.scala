package circuitbreaker

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * ActorDemo
  * Created by fabiotiriticco on 06/02/2017.
  */
class User(coffeeMachine: ActorRef) extends Actor with ActorLogging {

  // this stuff is executed as the actor comes to life
  log.info("User actor started")
  coffeeMachine ! GiveMeCaffeine(self, 2)

  // behaviour
  override def receive: Receive = {
    case HereIsYourCoffee(cups) =>
      log.info("received " + cups + " cup(s) of coffee")
      askForCoffee

    case SorrySomethingIsWrong() =>
      log.info("Wow that took a while, let me try again.")
      askForCoffee

    case OutOfOrder() =>
      log.info("received OutOfOrder, let me try again.")
      askForCoffee
  }

  def askForCoffee = context.system.scheduler.scheduleOnce(2.second, coffeeMachine, GiveMeCaffeine(self, 2))

}

// this is the companion object - it describes how the actor needs to be created
object User {
  def props(coffeeMachine: ActorRef) = Props(new User(coffeeMachine))
}

