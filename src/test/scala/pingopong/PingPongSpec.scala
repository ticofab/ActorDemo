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

package pingopong

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestKit, TestProbe}
import org.scalatest.WordSpecLike
import pingpong.{Ping, PingActor, Pong, PongActor}

import scala.concurrent.duration._

class PingPongSpec extends TestKit(ActorSystem("PingPong")) with WordSpecLike {

  "A Pong actor" should {

    "Send a pong back" in {
      val pongActorRef = TestActorRef[PongActor]
      val probe = TestProbe()
      probe.send(pongActorRef, Ping)
      probe.expectMsg(1.second, Pong(1))
    }

  }

  "A Ping actor" should {

    "Should immediately send a ping to the passed actor ref" in {
      val pongProbe = TestProbe("pongProbe")
      val pingActorRef = TestActorRef(Props(new PingActor(pongProbe.ref)))
      pongProbe.expectMsg(1.second, Ping)
      pongProbe.expectNoMsg(1.second)
    }

    "Send another Ping when it receives a Pong with value < 3" in {
      val pongProbe = TestProbe("pongProbe")
      val pingActorRef = TestActorRef(Props(new PingActor(pongProbe.ref)))
      pongProbe.expectMsg(1.second, Ping)

      val probe = TestProbe()
      probe.send(pingActorRef, Pong(1))
      pongProbe.expectMsg(1.second, Ping)

      probe.send(pingActorRef, Pong(2))
      pongProbe.expectMsg(1.second, Ping)

      probe.send(pingActorRef, Pong(0))
      pongProbe.expectMsg(1.second, Ping)

      probe.send(pingActorRef, Pong(-8))
      pongProbe.expectMsg(1.second, Ping)

    }

    "Send a poison pill if Pong contains a value higher than 3" in {
      val pongProbe = TestProbe("pongProbe")
      val pingActorRef = TestActorRef(Props(new PingActor(pongProbe.ref)))
      pongProbe.expectMsg(1.second, Ping)

      val probe = TestProbe()
      probe.watch(pongProbe.ref)
      probe.send(pingActorRef, Pong(4))
      probe.expectTerminated(pongProbe.ref, 1.second)

    }

  }

}
