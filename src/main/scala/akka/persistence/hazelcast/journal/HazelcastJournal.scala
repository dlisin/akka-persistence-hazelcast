package akka.persistence.hazelcast.journal

import akka.actor.ActorLogging
import akka.persistence.hazelcast.HazelcastExtension
import akka.persistence.hazelcast.journal.HazelcastJournal.{Message, MessageId}
import akka.persistence.journal.AsyncWriteJournal
import akka.persistence.{AtomicWrite, PersistentRepr}
import akka.serialization.SerializationExtension
import com.hazelcast.core.IMap

import scala.collection.immutable.Seq
import scala.concurrent.Future
import scala.util.Try


private[hazelcast] object HazelcastJournal {

  final case class MessageId(persistenceId: String, sequenceNumber: Long) extends Serializable

  final case class Message(persistenceId: String, sequenceNumber: Long, payload: Array[Byte]) extends Serializable {
    def this(messageId: MessageId, payload: Array[Byte]) = {
      this(persistenceId = messageId.persistenceId, sequenceNumber = messageId.sequenceNumber, payload)
    }
  }

}

class HazelcastJournal extends AsyncWriteJournal with ActorLogging
  with AllPersistenceIdsSubscribersTrait
  with PersistenceIdSubscribersTrait
  with TagSubscribersTrait {

  private val config = context.system.settings.config.getConfig("hazelcast-journal")

  private val serialization = SerializationExtension(context.system)
  private val hazelcast = HazelcastExtension.get(context.system)

  private val journalMap: IMap[MessageId, Message] = hazelcast.getMap(config.getString("map"))

  /**
    * Asynchronously writes a batch (`Seq`) of persistent messages to the journal.
    */
  override def asyncWriteMessages(messages: Seq[AtomicWrite]): Future[Seq[Try[Unit]]] = {
    log.debug(s"asyncWriteMessages(messages: '${messages.size}')")

    throw new UnsupportedOperationException("Not implemented")
  }

  /**
    * Asynchronously deletes all persistent messages up to `toSequenceNr` (inclusive).
    */
  override def asyncDeleteMessagesTo(persistenceId: String, toSequenceNr: Long): Future[Unit] = {
    log.debug(s"asyncDeleteMessagesTo(persistenceId: '$persistenceId', toSequenceNr: '$toSequenceNr')")

    throw new UnsupportedOperationException("Not implemented")
  }

  /**
    * Asynchronously reads the highest stored sequence number for the given `persistenceId`.
    * The persistent actor will use the highest sequence number after recovery as the starting point when persisting new events.
    */
  override def asyncReadHighestSequenceNr(persistenceId: String, fromSequenceNr: Long): Future[Long] = {
    log.debug(s"asyncReadHighestSequenceNr(persistenceId: '$persistenceId', fromSequenceNr: '$fromSequenceNr')")

    throw new UnsupportedOperationException("Not implemented")
  }

  /**
    * Asynchronously replays persistent messages. Implementations replay a message by calling `replayCallback`.
    * The returned future must be completed when all messages (matching the sequence number bounds) have been replayed.
    */
  override def asyncReplayMessages(persistenceId: String, fromSequenceNr: Long, toSequenceNr: Long,
                                   max: Long)(recoveryCallback: (PersistentRepr) => Unit): Future[Unit] = {
    log.debug(s"asyncReplayMessages(persistenceId: '$persistenceId', fromSequenceNr: '$fromSequenceNr', toSequenceNr: '$toSequenceNr', max: '$max')")

    throw new UnsupportedOperationException("Not implemented")
  }

  protected def persistentToBytes(p: PersistentRepr): Array[Byte] = serialization.serialize(p).get

  protected def persistentFromBytes(a: Array[Byte]): PersistentRepr = serialization.deserialize(a, classOf[PersistentRepr]).get


}


