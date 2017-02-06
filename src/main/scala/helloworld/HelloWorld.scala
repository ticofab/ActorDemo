package helloworld

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

/**
  * ActorDemo
  * Created by fabiotiriticco on 06/02/2017.
  */
object HelloWorld extends App {
  val as = ActorSystem()
  val pingActor = as.actorOf(Props(new PingActor))

  pingActor ! Ping
  pingActor ! Ping
  pingActor ! Ping
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