import akka.actor.SupervisorStrategy.{Restart, Resume}
import akka.actor.{Actor, ActorLogging, OneForOneStrategy, Props}

import scala.concurrent.duration._

/**
  * Created by fabiotiriticco on 30/06/2016.
  */
class Supervisor extends Actor with ActorLogging {

  val counterActor = context.actorOf(CoffeeMachine.props, "CoffeeMachine")
  val commanderActor = context.actorOf(User.props(counterActor), "User")

  // behaviour for failing children
  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 5.seconds) {

    case uoe: OutOfCoffeeException =>
      log.info(self.path.name + ", child failed!, restarting it.")
      Restart
  }

  def receive = {
    case _ => log.info(self.path.name + ", received something")
  }
}

object Supervisor {
  val props = Props[Supervisor]
}