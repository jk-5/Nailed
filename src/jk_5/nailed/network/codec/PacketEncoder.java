package jk_5.nailed.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.minecraft.src.Packet;

import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public class PacketEncoder extends MessageToMessageEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> out) throws Exception{
        ByteBuf outBuf = ctx.channel().alloc().buffer(packet.getPacketSize());
        DataOutput dataOut = new ByteBufOutputStream(outBuf);
        boolean success = false;
        try{
            outBuf.writeByte(packet.getPacketId());
            try {
                packet.writePacketData(dataOut);
            } catch (IOException ex) {
                throw new EncoderException(ex);
            }
            success = true;
            out.add(outBuf);
        }finally{
            if (!success) {
                outBuf.release();
            }
        }
    }
}
