import akka.actor.{Actor, ActorRef, Props}

/**
  * Created by fabiotiriticco on 30/06/2016.
  */
class CommanderActor(counterActor: ActorRef) extends Actor {

  // this stuff is executed as the actor comes to life
  println("myActor has been instantiated")
  counterActor ! IncrementByOne
  counterActor ! IncrementByOne
  counterActor ! IncrementByOne
  counterActor ! IncrementByOne
  counterActor ! IncrementByOne
  counterActor ! TellValue
  counterActor ! IncrementByValue(50) // this will make Fabio fail!
  counterActor ! TellValue

  // behaviour
  override def receive: Receive = {
    case ValueIs(value) => println("received value: " + value)
  }

}

// this is the companion object - it describes how the actor needs to be created
object CommanderActor {
  def props(counterActor: ActorRef) = Props(new CommanderActor(counterActor))
}


