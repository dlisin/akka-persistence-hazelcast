package akka.persistence.hazelcast.journal

import akka.persistence.CapabilityFlag
import akka.persistence.journal.JournalSpec
import com.typesafe.config.ConfigFactory

class HazelcastJournalSpec extends JournalSpec(ConfigFactory.load()) {
  override protected def supportsRejectingNonSerializableObjects: CapabilityFlag = true
}
