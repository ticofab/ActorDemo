import akka.actor.SupervisorStrategy.{Restart, Resume}
import akka.actor.{Actor, OneForOneStrategy, Props}

import scala.concurrent.duration._

/**
  * Created by fabiotiriticco on 30/06/2016.
  */
class Supervisor extends Actor {

  val counterActor = context.actorOf(CounterActor.props, "counter")
  val commanderActor = context.actorOf(CommanderActor.props(counterActor), "commander")

  // behaviour for failing children
  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 5.seconds) {

    case uoe: UnsupportedOperationException =>
      println(self + ", child failed!, restarting it.")
      Restart

    case ae: ArithmeticException =>
      println(self + ", child failed with an ArithmeticException. Resuming it.")
      Resume
  }

  def receive = {
    case _ => println(self + ", received something")
  }
}

object Supervisor {
  val props = Props[Supervisor]
}