import akka.actor.{Actor, ActorRef, ActorSystem, Props}

/**
  * Created by fabiotiriticco on 30/06/2016.
  */
object ActorDemo extends App {
  println("Hello Arnoud")

  val actorSystem = ActorSystem("playground")
  val superman = actorSystem.actorOf(Supervisor.props)

}

