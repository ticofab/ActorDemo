package letitcrash

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, ActorLogging, OneForOneStrategy, Props}

import scala.concurrent.duration._

/**
  * Created by fabiotiriticco on 30/06/2016.
  */
class Supervisor extends Actor with ActorLogging {

  val coffeeMachine = context.actorOf(CoffeeMachine.props, "CoffeeMachine")
  val user = context.actorOf(User.props(coffeeMachine), "User")

  // behaviour for failing children
  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 5.seconds) {

    case _: OutOfCoffeeException =>
      log.info("{} failed!, restarting it.", sender.path.name)
      Restart
  }

  def receive = {
    case _ => log.info("received something unexpected")
  }
}

object Supervisor {
  val props = Props[Supervisor]
}