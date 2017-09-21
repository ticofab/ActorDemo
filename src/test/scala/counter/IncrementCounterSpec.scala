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

package counter

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestKit, TestProbe}
import org.scalatest.WordSpecLike

import scala.concurrent.duration._

class IncrementCounterSpec extends TestKit(ActorSystem("Counter")) with WordSpecLike {

  "A Counter actor" should {

    "Increment its internal counter by one when told to" in {
      val counterActorRef = TestActorRef[CounterActor]
      assert(counterActorRef.underlyingActor.counter == 0)
      val probe = TestProbe()
      probe.send(counterActorRef, Increment)
      assert(counterActorRef.underlyingActor.counter == 1)
    }

  }

  "An incrementer actor" should {

    "Should immediately send 3 increment messages to the passed actor ref and then a get counter value " in {
      val counterProbe = TestProbe("counterProbe")
      val incrementerActorRef = TestActorRef(Props(new Incrementer(counterProbe.ref)))
      counterProbe.expectMsg(1.second, Increment)
      counterProbe.expectMsg(1.second, Increment)
      counterProbe.expectMsg(1.second, Increment)
      counterProbe.expectMsg(1.second, GetCounterValue)
      counterProbe.expectNoMsg(1.second)
    }

  }

}
