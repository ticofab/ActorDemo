package letitcrash

import akka.actor.{Actor, ActorRef, Props}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Created by fabiotiriticco on 30/06/2016.
  */
class User(coffeeMachine: ActorRef) extends Actor {

  // this stuff is executed as the actor comes to life
  println(self.path.name + ", " + "User actor started")
  coffeeMachine ! GiveMeCaffeine(self)

  // behaviour
  override def receive: Receive = {
    case HereIsYourCoffee(cups) =>
      println(self.path.name + ", received " + cups + " cup(s) of coffee")
      context.system.scheduler.scheduleOnce(2000.millisecond, coffeeMachine, GiveMeCaffeine(self))
  }

}

// this is the companion object - it describes how the actor needs to be created
object User {
  def props(coffeeMachine: ActorRef) = Props(new User(coffeeMachine))
}


