package letitcrash

import akka.actor.ActorRef

/**
  * Created by fabiotiriticco on 30/06/2016.
  */

// these are the messages that can be exchanged
case class GiveMeCaffeine(sender: ActorRef)
