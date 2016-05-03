package akka.persistence.hazelcast.journal.util

import akka.persistence.hazelcast.journal.HazelcastJournal._
import com.hazelcast.mapreduce.aggregation.PropertyExtractor

private[hazelcast] class HighestSequenceNrExtractor extends PropertyExtractor[Message, java.lang.Long] {
  override def extract(value: Message): java.lang.Long = value.sequenceNr
}
