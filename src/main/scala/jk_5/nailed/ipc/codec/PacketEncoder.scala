package jk_5.nailed.ipc.codec

import java.util
import jk_5.nailed.ipc.packet.IPCPacket
import io.netty.handler.codec.MessageToMessageEncoder
import io.netty.channel.ChannelHandlerContext
import com.nexus.data.json.JsonObject

/**
 * No description given
 *
 * @author jk-5
 */
class PacketEncoder extends MessageToMessageEncoder[IPCPacket] {

  override def encode(ctx: ChannelHandlerContext, packet: IPCPacket, out: util.List[AnyRef]){
    val data = new JsonObject
    packet.write(data)
    val packetData = new JsonObject
    packetData.add("name", packet.getName)
    if(packet.hasData) packetData.add("data", data)
    out.add(packetData)
  }
}
