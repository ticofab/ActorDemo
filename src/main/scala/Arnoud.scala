import akka.actor.{Actor, ActorRef, Props}

/**
  * Created by fabiotiriticco on 30/06/2016.
  */
class Arnoud(fabio: ActorRef) extends Actor {

  // this stuff is executed as the actor comes to life
  println("arnoud has been instantiated")
  fabio ! IncrementByOne
  fabio ! IncrementByOne
  fabio ! IncrementByOne
  fabio ! IncrementByOne
  fabio ! IncrementByOne
  fabio ! TellValue
  fabio ! IncrementByValue(50) // this will make Fabio fail!
  fabio ! TellValue

  // behaviour
  override def receive: Receive = {
    case ValueIs(value) => println("received value: " + value)
  }

}

// this is the companion object - it describes how the actor needs to be created
object Arnoud {
  def props(fabio: ActorRef) = Props(new Arnoud(fabio))
}


