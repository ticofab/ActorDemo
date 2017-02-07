package pingpong

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

// a ping message (no information attached)
case object Ping

// a pong message (carries information)
case class Pong(pings: Int)

class PongActor extends Actor {

  // internal state
  var pings = 0

  override def receive = {
    case Ping =>
      pings = pings + 1
      sender ! Pong(pings)
  }
}

class PingActor(pongActor: ActorRef) extends Actor {

  // send messages to ping actor
  pongActor ! Ping
  pongActor ! Ping
  pongActor ! Ping

  override def receive = {
    case Pong(pings) =>
      println(self.path.name + ", received Pong with " + pings)
      if (pings == 3) {
        // shut system down
        context.system.terminate()
      }
  }
}

// the program entry point
object PingPongApp extends App {

  // instantiate actor system
  val as = ActorSystem()

  // instantiate ping actor
  val pongActor = as.actorOf(Props(new PongActor), "pongActor")

  // instantiate supervisor
  as.actorOf(Props(new PingActor(pongActor)), "pingActor")

  // wait for signal to terminate
  Await.result(as.whenTerminated, Duration.Inf)
}
