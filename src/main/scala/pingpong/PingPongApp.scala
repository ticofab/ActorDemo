package pingpong

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

class PongActor extends Actor {

  // internal state
  var pings = 0

  // actor's behaviour
  override def receive = {
    case Ping =>
      println(self.path.name + ", received one ping.")
      pings = pings + 1
      sender ! Pong(pings)
  }
}

class PingActor(pongActor: ActorRef) extends Actor {

  // send messages to ping actor
  pongActor ! Ping

  override def receive = {
    case Pong(pings) =>
      println(self.path.name + ", received Pong with " + pings + " pings.")

      // if we received less than 3 pings back send another one
      if (pings < 3) pongActor ! Ping
  }
}

// a ping message (no information attached)
case object Ping

// a pong message (carries information)
case class Pong(pings: Int)

// the program entry point
object PingPongApp extends App {

  // instantiate actor system
  val as = ActorSystem()

  // instantiate ping actor
  val pongActor = as.actorOf(Props(new PongActor), "pongActor")

  // instantiate supervisor
  as.actorOf(Props(new PingActor(pongActor)), "pingActor")
}
