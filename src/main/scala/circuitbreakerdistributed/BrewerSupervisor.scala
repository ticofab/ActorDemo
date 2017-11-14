package circuitbreakerdistributed

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, OneForOneStrategy, Props}

import scala.concurrent.duration._

class BrewerSupervisor extends Actor {
  val coffeeBrewer = context.actorOf(CoffeeBrewer(), "brewer")
  coffeeBrewer ! Ready

  // behaviour for failing children
  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 5.seconds) {

    case _: OutOfCoffeeException =>
      println(self.path.name + ", " + sender.path.name + " failed!, restarting it.")
      Restart
  }

  def receive = Actor.emptyBehavior
}

object BrewerSupervisor {
  def apply(): Props = Props[BrewerSupervisor]
}
