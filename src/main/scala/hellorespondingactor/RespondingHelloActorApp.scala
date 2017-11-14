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
package hellorespondingactor

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.{AskTimeoutException, ask}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object RespondingHelloActorApp extends App {

  val actorSystem = ActorSystem("hello-actor")

  val helloActor = actorSystem.actorOf(Props(new RespondingHelloActor))

  val futureResponse = (helloActor ? "Hello, Actor!") (3.seconds).mapTo[String]
  futureResponse.recover {
    case t: AskTimeoutException => println("too late!")
  }

  futureResponse.foreach(s => {
    println("received a string back: " + s)
    actorSystem.terminate()
  })

  actorSystem.whenTerminated.foreach(t => println("system has terminated"))

}

class RespondingHelloActor extends Actor {
  override def receive = {
    case s: String =>
      println(self.path.name + ", I received: " + s + " from: " + sender)
      sender ! "thanks!"
  }
}

/*

  Key concepts:
    1. the ask pattern, useful for cases where a response is needed (like a web server)
    2. in order to receive a message, you need to be an actor

  Key take-aways:
    1. The sender of a message is only available if the sender is an actor itself.
    2. The ask pattern creates a temporary one-off actor for receiving a reply to a message and completes a Future
        with it; returns said Future.
    3. We need to terminate the actorSystem, but that has to be done in a way that takes into account completion of work
    4. try to create another actor system in there

  Patterns shown:
    1. ...

 */