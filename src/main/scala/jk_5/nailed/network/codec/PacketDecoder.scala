package jk_5.nailed.network.codec

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ReplayingDecoder
import jk_5.nailed.network.ReadState
import net.minecraft.server.MinecraftServer
import java.io.DataInput
import java.io.EOFException
import java.io.IOException
import java.util
import net.minecraft.network.packet.{Packet254ServerPing, Packet}

/**
 * No description given
 *
 * @author jk-5
 */
class PacketDecoder extends ReplayingDecoder[ReadState](ReadState.HEADER) {

  private var input: DataInput = null
  private var packet: Packet = null
  private var shutdown = false

  def decode(ctx: ChannelHandlerContext, in: ByteBuf, out: util.List[AnyRef]) {
    if(shutdown){
      in.readByte //Discard the last byte
      return
    }
    if(input == null){
      input = new ByteBufInputStream(in)
    }
    try {
      while (true){
        state match{
          case ReadState.HEADER =>
            val packetId: Int = input.readUnsignedByte
            packet = Packet.getNewPacket(MinecraftServer.getServer.getLogAgent, packetId)
            if (packet == null) {
              throw new IOException("Bad packet id " + packetId)
            }
            checkpoint(ReadState.DATA)
          case ReadState.DATA =>
            packet.readPacketData(input)
            checkpoint(ReadState.HEADER)
            out.add(packet)
            if (packet.isInstanceOf[Packet254ServerPing]) {
              shutdown = true
              return
            }
            packet = null
            return
          case _ => throw new IllegalStateException
        }
      }
    }
    catch {
      case ex: EOFException =>
    }
  }
}