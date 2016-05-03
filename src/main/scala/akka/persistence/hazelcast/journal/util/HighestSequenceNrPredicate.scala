package akka.persistence.hazelcast.journal.util

import java.util.Map.Entry

import akka.persistence.hazelcast.journal.HazelcastJournal._
import com.hazelcast.query.Predicate

private[hazelcast] class HighestSequenceNrPredicate(persistenceId: String, fromSequenceNr: Long)
  extends Predicate[MessageId, Message] {
  override def apply(entry: Entry[MessageId, Message]): Boolean = {
    val messageId = entry.getKey
    messageId.persistenceId == persistenceId && messageId.sequenceNr >= fromSequenceNr
  }
}
