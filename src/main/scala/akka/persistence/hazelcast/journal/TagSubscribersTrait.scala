package akka.persistence.hazelcast.journal

import akka.actor.ActorRef

import scala.collection.mutable

private[hazelcast] trait TagSubscribersTrait {
  private val tagSubscribers = new mutable.HashMap[String, mutable.Set[ActorRef]] with mutable.MultiMap[String, ActorRef]

  protected def hasTagSubscribers: Boolean = tagSubscribers.nonEmpty
}
