package akka.persistence.hazelcast.journal

import akka.actor.ActorLogging
import akka.persistence.hazelcast.HazelcastExtension
import akka.persistence.journal.AsyncWriteJournal
import akka.persistence.{AtomicWrite, PersistentRepr}
import akka.serialization.SerializationExtension
import com.hazelcast.core.IMap

import scala.collection.immutable.Seq
import scala.concurrent.Future
import scala.util.Try


class HazelcastJournal extends AsyncWriteJournal with ActorLogging {
  val config = context.system.settings.config.getConfig("hazelcast-journal")

  val hazelcast = HazelcastExtension.get(context.system)
  val serialization = SerializationExtension(context.system)

  private val journal: IMap[MessageId, Message] = hazelcast.getMap(config.getString("map"))

  /**
    * Asynchronously writes a batch (`Seq`) of persistent messages to the journal.
    */
  override def asyncWriteMessages(messages: Seq[AtomicWrite]): Future[Seq[Try[Unit]]] = {
    log.debug(s"Writing '${entries.size}' messages for persistenceId '${atomicWrite.persistenceId}'.")

    messages.map(atomicWrite => Try {
      val entries = atomicWrite.payload.map(persistentRepr => {
        val id = MessageId(atomicWrite.persistenceId, persistentRepr.sequenceNr)
        (id, Message(serialization.serialize(persistentRepr).get, id))
      }).toMap
      log.debug(s"Writing '${entries.size}' messages for persistenceId '${atomicWrite.persistenceId}'.")
      journal.putAll(entries)
      log.debug(s"'${entries.size}' messages have been written for persistenceId '${atomicWrite.persistenceId}'.")
    })
  }

  /**
    * Asynchronously deletes all persistent messages up to `toSequenceNr` (inclusive).
    */
  override def asyncDeleteMessagesTo(persistenceId: String, toSequenceNr: Long): Future[Unit] = {
    throw new UnsupportedOperationException("Not implemented")
  }

  /**
    * Plugin API: asynchronously reads the highest stored sequence number for the
    * given `persistenceId`. The persistent actor will use the highest sequence
    * number after recovery as the starting point when persisting new events.
    * This sequence number is also used as `toSequenceNr` in subsequent call
    * to [[#asyncReplayMessages]] unless the user has specified a lower `toSequenceNr`.
    * Journal must maintain the highest sequence number and never decrease it.
    */
  override def asyncReadHighestSequenceNr(persistenceId: String, fromSequenceNr: Long): Future[Long] = {
    throw new UnsupportedOperationException("Not implemented")
  }

  /**
    * Asynchronously replays persistent messages. Implementations replay
    * a message by calling `replayCallback`. The returned future must be completed
    * when all messages (matching the sequence number bounds) have been replayed.
    * The future must be completed with a failure if any of the persistent messages
    * could not be replayed.
    */
  override def asyncReplayMessages(persistenceId: String, fromSequenceNr: Long, toSequenceNr: Long,
                                   max: Long)(recoveryCallback: (PersistentRepr) => Unit): Future[Unit] = {
    throw new UnsupportedOperationException("Not implemented")
  }
}

private[hazelcast] case class MessageId(persistenceId: String, sequenceNumber: Long) extends Serializable

private[hazelcast] case class Message(persistenceId: String, sequenceNumber: Long, payload: Array[Byte]) extends Serializable {
  def this(messageId: MessageId, payload: Array[Byte]) = {
    this(persistenceId = messageId.persistenceId, sequenceNumber = messageId.sequenceNumber, payload)
  }
}
