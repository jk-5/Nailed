package jk_5.nailed.ipc.packet

import com.nexus.data.json.JsonObject

/**
 * No description given
 *
 * @author jk-5
 */
class EmptyPayloadPacket {

  @inline final def write(data: JsonObject) = {}
  @inline final def read(data: JsonObject) = {}
  @inline final def hasData = false
}
