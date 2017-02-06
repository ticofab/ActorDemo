package helloworld

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * ActorDemo
  * Created by fabiotiriticco on 06/02/2017.
  */
object HelloWorld extends App {

  // instantiate actor system
  val as = ActorSystem()

  // instantiate actor
  val pingActor = as.actorOf(Props(new PingActor))

  // send messages to actor
  pingActor ! Ping
  pingActor ! Ping
  pingActor ! Ping

  // terminate the actor system
  as.terminate()
  Await.result(as.whenTerminated, Duration.Inf)
}

case object Ping

case object Pong

class PingActor extends Actor with ActorLogging {
  var pings = 0

  override def receive = {
    case Ping =>
      pings = pings + 1
      log.info("received {} Ping", pings)
  }
}