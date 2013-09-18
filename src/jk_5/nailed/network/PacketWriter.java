package jk_5.nailed.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.Channel;
import io.netty.handler.codec.EncoderException;
import net.minecraft.src.Packet;
import net.minecraft.src.NetLoginHandler;

import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * No description given
 *
 * @author jk-5
 */
public class PacketWriter {

    private static final int FLUSH_TIME = 1;
    long lastFlush;
    private final Queue<Packet> queue = new ArrayDeque<Packet>(64);

    void write(Channel channel, NettyNetworkManager networkManager, Packet msg) {
        queue.add(msg);

        if (!(networkManager.connection instanceof NetLoginHandler) && System.currentTimeMillis() - lastFlush < FLUSH_TIME) {
            return;
        }
        lastFlush = System.currentTimeMillis();

        int estimatedSize = 0;
        for (Packet packet : queue) {
            estimatedSize += packet.getPacketSize();
        }
        ByteBuf outBuf = channel.alloc().buffer(estimatedSize);
        DataOutput dataOut = new ByteBufOutputStream(outBuf);
        boolean success = false;

        try {
            for (Packet packet : queue) {
                outBuf.writeByte(packet.getPacketId());
                try {
                    packet.writePacketData(dataOut);
                } catch (IOException ex) {
                    throw new EncoderException(ex);
                }
            }
            success = true;
            channel.writeAndFlush(outBuf);
        } finally {
            queue.clear();
            if (!success) {
                outBuf.release();
            }
        }
    }
}