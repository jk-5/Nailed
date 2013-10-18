package jk_5.nailed.network.codec

import io.netty.handler.codec.{EncoderException, MessageToMessageEncoder}
import java.util
import io.netty.channel.ChannelHandlerContext
import java.io.IOException
import io.netty.buffer.ByteBufOutputStream
import io.netty.channel.ChannelHandler.Sharable
import net.minecraft.network.packet.Packet

/**
 * No description given
 *
 * @author jk-5
 */
@Sharable
object PacketEncoder extends MessageToMessageEncoder[Packet] {

  override def encode(ctx: ChannelHandlerContext, packet: Packet, out: util.List[AnyRef]){
    val outBuf = ctx.channel().alloc().buffer(packet.getPacketSize)
    val dataOut = new ByteBufOutputStream(outBuf)
    var success = false
    try{
      outBuf.writeByte(packet.getPacketId)
      try {
        packet.writePacketData(dataOut)
      }catch{
        case e: IOException => throw new EncoderException(e)
      }
      success = true
      out.add(outBuf)
    }finally{
      if (!success) outBuf.release()
    }
  }
}
