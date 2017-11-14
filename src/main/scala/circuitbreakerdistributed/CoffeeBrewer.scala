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

package circuitbreakerdistributed

import akka.actor.{Actor, Props}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

case object Ready

case class BrewCoffee()

case class CoffeeBrewed()

case class OutOfCoffeeException() extends Exception

class CoffeeBrewer extends Actor {

  println(self.path + " starting.")

  // internal state
  var caffeineReserve = 0

  override def receive = startingUp

  def startingUp: Receive = {
    case Ready =>
      println(self.path.name + ", ready!")
      caffeineReserve = 10
      context become ready

    case _ => println(self.path.name + ", someone is asking for coffee but I'm not ready, so I won't respond")
  }

  def ready: Receive = {
    case BrewCoffee =>

      if (caffeineReserve == 0) {
        println(self + ", out of coffee!")
        throw OutOfCoffeeException()
      }

      println(self.path.name + ", " + "brewing coffee")
      caffeineReserve -= 1
      sender ! CoffeeBrewed()
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println(self.path.name + ", starting up.. it will take me 20 seconds")
    context.system.scheduler.scheduleOnce(20.seconds, self, Ready)
    super.preRestart(reason, message)
  }
}

object CoffeeBrewer {
  def apply() = Props(new CoffeeBrewer)
}
