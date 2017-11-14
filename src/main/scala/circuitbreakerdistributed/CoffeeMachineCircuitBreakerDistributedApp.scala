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

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object CoffeeMachineCircuitBreakerDistributedApp extends App {
  println("ActorDemoCircuitBreaker starting")
  val config = ConfigFactory.load("circuitbreakerdistributed.conf")
  val actorSystem = ActorSystem("playground", config)

  val c = config.getStringList("akka.cluster.roles")

  if (c.contains("machine")) actorSystem.actorOf(Supervisor(), "supervisor")
  else actorSystem.actorOf(BrewerSupervisor(), "brewerSupervisor")
}
