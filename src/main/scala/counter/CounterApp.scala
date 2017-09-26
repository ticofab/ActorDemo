/**
  * Copyright 2017 Fabio Tiriticco, Fabway
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package counter

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

// ---------------------------------------
// ------ Messages to be exchanged -------
// ---------------------------------------

// an increment message (no information attached)
case object Increment

// a message to ask the counter value
case object GetCounterValue

// a counter value message (carries information)
case class CounterValue(value: Int)

// -------------------------------------
// ------ Actor implementations --------
// -------------------------------------

class CounterActor extends Actor {

  // internal state
  var counter = 0

  // actor's behaviour
  override def receive = {
    case Increment =>
      println(self.path.name + ": received one increment message.")
      counter = counter + 1

    case GetCounterValue =>
      println(self.path.name + ": received a get counter value message.")
      sender ! CounterValue(counter) // sends a copy of counter
  }

}

class Incrementer(counterActor: ActorRef) extends Actor {

  // send messages to counter actor
  counterActor ! Increment
  counterActor ! Increment
  counterActor ! Increment
  counterActor ! GetCounterValue

  override def receive = {
    case CounterValue(value) =>
      println(self.path.name + ": received counter value with value: " + value)
  }
}

// -----------------------------------
// ------ The app entry point --------
// -----------------------------------

object CounterApp extends App {

  // instantiate actor system
  val as = ActorSystem()

  // instantiate counter actor
  val counterActor = as.actorOf(Props(new CounterActor), "counter")

  // instantiate incrementer
  as.actorOf(Props(new Incrementer(counterActor)), "incrementer")

}
