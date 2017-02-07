package letitcrash

import akka.actor.ActorSystem

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by fabiotiriticco on 30/06/2016.
  */
object CoffeeMachineApp extends App {
  println("ActorDemo starting")
  val as = ActorSystem("playground")
  val supervisor = as.actorOf(Supervisor.props, "supervisor")

  // wait for signal to terminate
  Await.result(as.whenTerminated, Duration.Inf)
}
