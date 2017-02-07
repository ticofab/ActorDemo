package helloworld

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

// a ping (no information attached)
case object Ping

// a pong class (carries information)
case class Pong(pings: Int)

// the ping actor
class PingActor extends Actor with ActorLogging {

  // internal state
  var pings = 0

  override def receive = {
    case Ping =>
      pings = pings + 1
      sender ! Pong(pings)
  }
}

// the supervisor
class Supervisor extends Actor with ActorLogging {

  // instantiate ping actor
  val pingActor = context.actorOf(Props(new PingActor))

  // send messages to ping actor
  pingActor ! Ping
  pingActor ! Ping
  pingActor ! Ping

  override def receive = {
    case Pong(pings) => log.info("received Pong with {}", pings)
  }
}

// the program entry point
object HelloWorld extends App {

  // instantiate actor system
  val as = ActorSystem()

  // instantiate supervisor
  as.actorOf(Props(new Supervisor), "supervisor")

  // terminate the actor system
  as.terminate()
  Await.result(as.whenTerminated, Duration.Inf)
}
