package akka.persistence.hazelcast.journal

import akka.actor.ActorRef

import scala.collection.mutable

private[hazelcast] trait PersistenceIdSubscribersTrait {
  private val persistenceIdSubscribers = new mutable.HashMap[String, mutable.Set[ActorRef]] with mutable.MultiMap[String, ActorRef]

  protected def hasPersistenceIdSubscribers: Boolean = persistenceIdSubscribers.nonEmpty
}
