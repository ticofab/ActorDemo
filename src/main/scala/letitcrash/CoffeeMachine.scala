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

package letitcrash

import akka.actor.{Actor, ActorRef, Props}

// these are the messages that can be exchanged
case class GiveMeCaffeine(sender: ActorRef)

case class HereIsYourCoffee(numberOfCups: Int)

case class OutOfCoffeeException() extends Exception

class CoffeeMachine extends Actor {

  // internal state
  var caffeineReserve = 4

  println(self.path.name + ", started. caffeine reserve: " + caffeineReserve)

  // behaviour
  override def receive: Receive = {

    case GiveMeCaffeine(user) =>
      println(self.path.name + ", received GiveMeCaffeine")
      if (caffeineReserve == 0) {
        println(self.path.name + ", out of coffee!")
        throw OutOfCoffeeException()
      }

      // send nicotine back to our sender
      caffeineReserve -= 1
      user ! HereIsYourCoffee(1)

  }

  override def preRestart(reason: Throwable, message: Option[Any]) {
    println(self.path.name + ", about to restart")
    super.preRestart(reason, message)
    message.foreach(self ! _)
  }

}

object CoffeeMachine {
  def apply() = Props[CoffeeMachine]
}
