package circuitbreaker

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, OneForOneStrategy, Props}

import scala.concurrent.duration._

/**
  * ActorDemo
  * Created by fabiotiriticco on 06/02/2017.
  */
class Supervisor extends Actor {

  val coffeeBrewer = context.actorOf(CoffeeBrewer(), "CoffeeBrewer")
  val coffeeMachine = context.actorOf(CoffeeMachine(coffeeBrewer), "CoffeeMachine")
  val user = context.actorOf(User(coffeeMachine), "User")

  // behaviour for failing children
  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 5.seconds) {

    case _: OutOfCoffeeException =>
      println(self.path.name + ", " + sender.path.name + " failed!, restarting it.")
      Restart
  }

  def receive = {
    case _ => println(self.path.name + ", " + "received something unexpected")
  }
}

object Supervisor {
  val props = Props[Supervisor]
}
