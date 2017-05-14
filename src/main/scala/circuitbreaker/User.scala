package circuitbreaker

import akka.actor.{Actor, ActorRef, Props}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * ActorDemo
  * Created by fabiotiriticco on 06/02/2017.
  */
class User(coffeeMachine: ActorRef) extends Actor {

  // this stuff is executed as the actor comes to life
  println(self.path.name + ", " + "User actor started")
  coffeeMachine ! GiveMeCaffeine(self, 2)

  // behaviour
  override def receive: Receive = {
    case HereIsYourCoffee(cups) =>
      println(self.path.name + ", " + "received " + cups + " cup of coffee")
      askForCoffee

    case SorrySomethingIsWrong() =>
      println(self.path.name + ", " + "Wow that took a while, let me try again.")
      askForCoffee

    case OutOfOrder() =>
      println(self.path.name + ", " + "received OutOfOrder, let me try again.")
      askForCoffee
  }

  def askForCoffee = context.system.scheduler.scheduleOnce(2.second, coffeeMachine, GiveMeCaffeine(self, 2))

}

// this is the companion object - it describes how the actor needs to be created
object User {
  def apply(coffeeMachine: ActorRef) = Props(new User(coffeeMachine))
}

