import akka.actor.SupervisorStrategy.{Restart, Resume}
import akka.actor.{Actor, ActorLogging, OneForOneStrategy, Props}

import scala.concurrent.duration._

/**
  * Created by fabiotiriticco on 30/06/2016.
  */
class Supervisor extends Actor with ActorLogging {

  val coffeeMachine = context.actorOf(CoffeeMachine.props, "CoffeeMachine")
  val user = context.actorOf(User.props(coffeeMachine), "User")

  var amountOfRestarts = 0

  // behaviour for failing children
  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 5.seconds) {

    case _: OutOfCoffeeException =>
      amountOfRestarts = amountOfRestarts + 1
      log.info("{}, {} failed!, restarting it for the {} time", self.path.name, sender.toString, amountOfRestarts)
      sender ! RechargeCoffee(5)
      Restart
  }

  def receive = {
    case _ => log.info(self.path.name + ", received something")
  }
}

object Supervisor {
  val props = Props[Supervisor]
}