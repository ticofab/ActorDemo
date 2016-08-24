import akka.actor.{Actor, ActorRef, Props}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by fabiotiriticco on 30/06/2016.
  */
class CommanderActor(counterActor: ActorRef) extends Actor {

  // this stuff is executed as the actor comes to life
  println("myActor has been instantiated")
  counterActor ! IncrementByOne
  counterActor ! IncrementByOne
  counterActor ! IncrementByValue(3)
  counterActor ! TellValue
  context.system.scheduler.scheduleOnce(1000.millisecond, counterActor, IncrementByValue(-2))
  context.system.scheduler.scheduleOnce(2000.millisecond, counterActor, TellValue)
  context.system.scheduler.scheduleOnce(3000.millisecond, counterActor, IncrementByValue(50))
  context.system.scheduler.scheduleOnce(4000.millisecond, counterActor, TellValue)

  // behaviour
  override def receive: Receive = {
    case ValueIs(value) => println(self + ", received value: " + value)
  }

}

// this is the companion object - it describes how the actor needs to be created
object CommanderActor {
  def props(counterActor: ActorRef) = Props(new CommanderActor(counterActor))
}


