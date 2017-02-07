package helloworld

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

// a ping (no information attached)
case object Ping

// a pong class (carries information)
case class Pong(pings: Int)

// the ping actor
class PingActor extends Actor {

  // internal state
  var pings = 0

  override def receive = {
    case Ping =>
      pings = pings + 1
      sender ! Pong(pings)
  }
}

// the supervisor
class PongActor(pingActor: ActorRef) extends Actor {

  // send messages to ping actor
  pingActor ! Ping
  pingActor ! Ping
  pingActor ! Ping

  override def receive = {
    case Pong(pings) => println(self.path.name + ", received Pong with " + pings)
  }
}

// the program entry point
object HelloWorld extends App {

  // instantiate actor system
  val as = ActorSystem()

  // instantiate ping actor
  val pingActor = as.actorOf(Props(new PingActor), "pingActor")

  // instantiate supervisor
  as.actorOf(Props(new PongActor(pingActor)), "pongActor")

  // terminate the actor system
  as.terminate()
  Await.result(as.whenTerminated, Duration.Inf)
}
