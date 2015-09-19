package akka.persistence.hazelcast

import akka.actor._
import akka.event.Logging
import com.hazelcast.config.{ClasspathXmlConfig, Config}
import com.hazelcast.core.{Hazelcast => HazelcastInstance, IMap}

object Hazelcast extends ExtensionId[Hazelcast] with ExtensionIdProvider {

  override def lookup() = Hazelcast

  override def createExtension(system: ExtendedActorSystem): Hazelcast = {
    new Hazelcast(system)
  }

  /**
   * Java API: retrieve the Hazelcast extension for the given system.
   */
  override def get(system: ActorSystem): Hazelcast = super.get(system)
}

class Hazelcast(system: ExtendedActorSystem) extends Extension {

  private lazy val logger = Logging.getLogger(system, this)
  private lazy val extensionConfig = system.settings.config.getConfig("hazelcast")

  private lazy val hazelcastInstance = {
    logger.info("Creating Hazelcast instance")

    val hazelcastConfigFile = extensionConfig.getString("config-file")
    logger.info("Using configuration file: {}", hazelcastConfigFile)

    val hazelcastConfig: Config = new ClasspathXmlConfig(hazelcastConfigFile)
    HazelcastInstance.newHazelcastInstance(hazelcastConfig)
  }

  /**
   * Delegate for HazelcastInstance.getMap
   */
  def getMap[K, V](name: String): IMap[K, V] = {
    hazelcastInstance.getMap(name)
  }
}
