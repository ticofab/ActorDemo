import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by fabiotiriticco on 30/06/2016.
  */
class User(coffeeMachine: ActorRef) extends Actor with ActorLogging {

  // this stuff is executed as the actor comes to life
  log.info("User actor started")
  coffeeMachine ! GiveMeCaffeine(self)

  // behaviour
  override def receive: Receive = {
    case HereIsYourCoffee(cups) =>
      log.info(self.path.name + ", received " + cups + " cup(s) of coffee")
      context.system.scheduler.scheduleOnce(2000.millisecond, coffeeMachine, GiveMeCaffeine(self))
  }

}

// this is the companion object - it describes how the actor needs to be created
object User {
  def props(coffeeMachine: ActorRef) = Props(new User(coffeeMachine))
}


