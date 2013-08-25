package jk_5.nailed.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import jk_5.nailed.network.packet.Packet;
import jk_5.nailed.network.packet.PacketSYN;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class IPCHandler extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext ctx;

    public void sendPacket(Packet packet) {
        this.ctx.writeAndFlush(packet.getSendBuffer());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.sendPacket(new PacketSYN());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Packet packet = Packet.getPacket((ByteBuf) msg);
        if (packet == null) return;
        packet.processPacket();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
