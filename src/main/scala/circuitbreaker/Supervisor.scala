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

package circuitbreaker

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, OneForOneStrategy, Props}

import scala.concurrent.duration._

class Supervisor extends Actor {

  val coffeeBrewer = context.actorOf(CoffeeBrewer(), "CoffeeBrewer")
  val coffeeMachine = context.actorOf(CoffeeMachine(coffeeBrewer), "CoffeeMachine")
  val user = context.actorOf(User(coffeeMachine), "User")

  // behaviour for failing children
  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 5.seconds) {

    case _: OutOfCoffeeException =>
      println(self.path.name + ", " + sender.path.name + " failed!, restarting it.")
      Restart
  }

  def receive = {
    case _ => println(self.path.name + ", " + "received something unexpected")
  }
}

object Supervisor {
  val props = Props[Supervisor]
}
