package akka.persistence.hazelcast

import akka.actor._
import akka.event.Logging
import com.hazelcast.config._
import com.hazelcast.core._

object HazelcastExtension extends ExtensionId[HazelcastExtension] with ExtensionIdProvider {

  override def lookup(): ExtensionId[_ <: Extension] = HazelcastExtension

  override def createExtension(system: ExtendedActorSystem): HazelcastExtension = new HazelcastExtension(system)

  /**
    * Java API: retrieve the Hazelcast extension for the given system.
    */
  override def get(system: ActorSystem): HazelcastExtension = super.get(system)
}

class HazelcastExtension(system: ExtendedActorSystem) extends Extension {

  private val logger = Logging.getLogger(system, this)
  private val config = system.settings.config.getConfig("akka.persistence.hazelcast")

  private val hazelcastInstance = {
    val hazelcastConfigFile = config.getString("config-file")

    logger.info(s"Using configuration file: '$hazelcastConfigFile'.")
    val hazelcastConfig: Config = new ClasspathXmlConfig(hazelcastConfigFile)

    logger.info(s"Creating Hazelcast instance")
    Hazelcast.newHazelcastInstance(hazelcastConfig)
  }

  /**
    * Delegate for HazelcastInstance.getMap
    */
  def getMap[K, V](name: String): IMap[K, V] = hazelcastInstance.getMap(name)
}
