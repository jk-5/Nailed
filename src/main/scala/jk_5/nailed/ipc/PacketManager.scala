package jk_5.nailed.ipc

import scala.collection.immutable.HashMap
import jk_5.nailed.ipc.packet._

/**
 * No description given
 *
 * @author jk-5
 */
object PacketManager {

  private final val packets = HashMap[String, Class[_ <: IPCPacket]](
    "join" -> classOf[PacketPlayerJoin],
    "leave" -> classOf[PacketPlayerLeave],
    "init" -> classOf[PacketInitConnection]
  )

  def getPacketName(cl: Class[_ <: IPCPacket]): Option[String] = {
    val option = this.packets.find(_._2 == cl)
    option match {
      case None => None
      case s: Some[_] => Some(option.get._1)
    }
  }
  def getPacket(name: String): Option[IPCPacket] = {
    val option = this.packets.find(_._1 == name)
    option match {
      case None => None
      case s: Some[_] => Some(option.get._2.newInstance())
    }
  }
}
