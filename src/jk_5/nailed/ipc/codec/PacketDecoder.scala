package jk_5.nailed.ipc.codec

import com.nexus.data.json.JsonObject
import io.netty.handler.codec.MessageToMessageDecoder
import java.util
import io.netty.channel.ChannelHandlerContext
import jk_5.nailed.ipc.packet.IPCPacket
import jk_5.nailed.ipc.PacketManager

/**
 * No description given
 *
 * @author jk-5
 */
class PacketDecoder extends MessageToMessageDecoder[JsonObject] {

  override def decode(ctx: ChannelHandlerContext, data: JsonObject, out: util.List[AnyRef]){
    if(data == null || data.get("name") == null){
      out.add(data)
      return
    }
    val packet = PacketManager.getPacket(data.get("name").asString)
    if(packet.isEmpty){
      out.add(data)
      return
    }
    if(data.get("data") != null){
      packet.get.read(data.get("data").asObject)
    }
    out.add(packet)
  }
}
