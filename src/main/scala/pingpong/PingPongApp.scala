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

package pingpong

import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}

class PongActor extends Actor {

  // internal state
  var receivedPings = 0

  // actor's behaviour
  override def receive = {
    case Ping =>
      println(self.path.name + ", received one ping.")
      receivedPings = receivedPings + 1
      sender ! Pong(receivedPings)
  }

  // actor lifecycle (partial)
  override def postStop() = {
    println("postStop")
  }
}

class PingActor(pongActor: ActorRef) extends Actor {

  // send messages to ping actor
  pongActor ! Ping

  override def receive = {
    case Pong(value) =>
      println(self.path.name + ", received Pong with value: " + value)

      // if we received less than 3 value send back another one, otherwise poison pill
      if (value < 3) pongActor ! Ping
      else pongActor ! PoisonPill
  }
}

// a ping message (no information attached)
case object Ping

// a pong message (carries information)
case class Pong(pings: Int)

// the program entry point
object PingPongApp extends App {

  // instantiate actor system
  val as = ActorSystem()

  // instantiate ping actor
  val pongActor = as.actorOf(Props(new PongActor), "pongActor")

  // instantiate supervisor
  as.actorOf(Props(new PingActor(pongActor)), "pingActor")

}
