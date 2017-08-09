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

import akka.actor.ActorSystem

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object CoffeeMachineApp extends App {
  println("ActorDemo starting")
  val as = ActorSystem("playground")
  val supervisor = as.actorOf(Supervisor.props, "supervisor")

  // wait for signal to terminate
  Await.result(as.whenTerminated, Duration.Inf)
}
