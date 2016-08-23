import akka.actor.SupervisorStrategy.Restart
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

    case _: RuntimeException =>
      println(self + ", child failed!, restarting it.")
      Restart

  }

  def receive = {
    case _ => println(self + ", received something")
  }
}

object Supervisor {
  val props = Props[Supervisor]
}