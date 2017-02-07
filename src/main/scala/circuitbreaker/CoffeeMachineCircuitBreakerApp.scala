package circuitbreaker

import akka.actor.ActorSystem

/**
  * ActorDemo
  * Created by fabiotiriticco on 06/02/2017.
  */
object CoffeeMachineCircuitBreakerApp extends App {
  println("ActorDemoCircuitBreaker starting")
  val actorSystem = ActorSystem("playground")
  val supervisor = actorSystem.actorOf(Supervisor.props, "supervisor")
}
