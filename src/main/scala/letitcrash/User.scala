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

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class User(coffeeMachine: ActorRef) extends Actor {

  // this stuff is executed as the actor comes to life
  println(self.path.name + ", " + "User actor started")
  coffeeMachine ! GiveMeCaffeine(self)

  // behaviour
  override def receive: Receive = {
    case HereIsYourCoffee(cups) =>
      println(self.path.name + ", received " + cups + " cup(s) of coffee")
      context.system.scheduler.scheduleOnce(2000.millisecond, coffeeMachine, GiveMeCaffeine(self))
  }

}

// this is the companion object - it describes how the actor needs to be created
object User {
  def apply(coffeeMachine: ActorRef) = Props(new User(coffeeMachine))
}


