import akka.actor.{Actor, Props}

/**
  * Created by fabiotiriticco on 30/06/2016.
  */

// these are the messages that can be exchanged
case object IncrementByOne

case class IncrementByValue(value: Int)

case object TellValue

case class ValueIs(value: Int)
// ------------------------

class CounterActor extends Actor {

  // internal state
  var count = 0

  // behaviour
  override def receive: Receive = {

    case IncrementByOne =>
      println(self + ", received IncrementByOne")
      count = count + 1

    case IncrementByValue(value) =>
      // if we are told to increment the state by more than 20 units, we crash
      if (value > 20) {
        println(self + ", received a too big value! failing.")
        throw new RuntimeException
      }
      count = count + value

    case TellValue =>
      // send our value back to our sender
      println(self + ", received TellValue")
      sender ! ValueIs(count)

    case _ => println("unknown message")

  }

}

object CounterActor {
  def props = Props[CounterActor]
}
