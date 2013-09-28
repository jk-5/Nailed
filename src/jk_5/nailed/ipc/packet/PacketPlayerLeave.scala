package jk_5.nailed.ipc.packet

import com.nexus.data.json.JsonObject

/**
 * No description given
 *
 * @author jk-5
 */
class PacketPlayerLeave(var username: String = "") extends IPCPacket {

  def write(data: JsonObject){
    data.add("username", this.username)
  }
  @inline def read(data: JsonObject) = NoOp
  @inline def process() = NoOp
}
