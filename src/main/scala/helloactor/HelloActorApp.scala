/**
  * Copyright 2017 Fabio Tiriticco - Fabway
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
package helloactor

import akka.actor.{Actor, ActorSystem, Props}

object HelloActorApp extends App {

  val actorSystem = ActorSystem("hello-actor")

  val helloActor = actorSystem.actorOf(Props(new HelloActor), "hello-actor")

  helloActor ! "Hello, Actor!"

}

class HelloActor extends Actor {
  override def receive = {
    case s: String => println(self.path.name + ", I received: " + s)
  }
}

/*

  Key concepts:

    1. what is an actor system
    2. try to check the sender of the message --> not defined + explanation of DeadLetters
    3. the actor reference
    4. actor creation via props
    5. the tell pattern - actor messages are untyped (mention typed actors)
    6. the receive method

  Patterns shown:
    1. ...

 */
