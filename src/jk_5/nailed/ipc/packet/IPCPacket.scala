package jk_5.nailed.ipc.packet

import com.nexus.data.json.JsonObject
import jk_5.nailed.ipc.PacketManager

/**
 * No description given
 *
 * @author jk-5
 */
abstract class IPCPacket {

  @inline def NoOp = throw new UnsupportedOperationException

  @inline def write(data: JsonObject)
  @inline def read(data: JsonObject)
  @inline def process()
  @inline def hasData = true

  @inline final def getName = PacketManager.getPacketName(this.getClass).get
}
