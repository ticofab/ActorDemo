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

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.{AskTimeoutException, CircuitBreaker, CircuitBreakerOpenException, ask}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

// these are the messages that can be exchanged
case class GiveMeCaffeine(sender: ActorRef)

case class HereIsYourCoffee(numberOfCups: Int)

case class InvalidAmount(euros: Int)

case class SorrySomethingIsWrong()

case class OutOfOrder()

class CoffeeMachine(coffeeBrewer: ActorRef) extends Actor {

  val breaker = CircuitBreaker(
    context.system.scheduler,
    1,
    5.second,
    10.second)
    .onOpen(println(self.path.name + ", Circuit Breaker is open"))
    .onClose(println(self.path.name + ", Circuit Breaker is closed"))
    .onHalfOpen(println(self.path.name + ", Circuit Breaker is half open, next message will go through"))

  // behaviour
  override def receive: Receive = {

    case GiveMeCaffeine(user) =>
      // let's see if we can brew coffee
      breaker
        .withCircuitBreaker((coffeeBrewer ? BrewCoffee) (4.second))
        .recover {

          case t: AskTimeoutException =>
            println(self.path.name + ", the coffee brewer timed out")
            user ! SorrySomethingIsWrong()

          case co: CircuitBreakerOpenException =>
            println(self.path.name + ", circuit breaker open")
            user ! OutOfOrder()

        }
        .mapTo[CoffeeBrewed]
        .foreach(cb => {
          user ! HereIsYourCoffee(1)
        })

  }


}

object CoffeeMachine {
  def apply(coffeeBrewer: ActorRef) = Props(new CoffeeMachine(coffeeBrewer))
}
