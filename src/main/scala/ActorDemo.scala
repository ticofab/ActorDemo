import akka.actor.ActorSystem

/**
  * Created by fabiotiriticco on 30/06/2016.
  */
object ActorDemo extends App {
  println("ActorDemo starting")
  val actorSystem = ActorSystem("playground")
  val supervisor = actorSystem.actorOf(Supervisor.props, "supervisor")
}

