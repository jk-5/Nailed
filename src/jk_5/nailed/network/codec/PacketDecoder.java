package jk_5.nailed.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import jk_5.nailed.network.ReadState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet254ServerPing;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public class PacketDecoder extends ReplayingDecoder<ReadState> {

    private DataInput input;
    private Packet packet;
    private boolean shutdown;

    public PacketDecoder() {
        super(ReadState.HEADER);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (shutdown) {
            in.readByte(); // Discard
            return;
        }

        if (input == null) {
            input = new ByteBufInputStream(in);
        }

        try {
            while (true) {
                switch (state()) {
                    case HEADER:
                        int packetId = input.readUnsignedByte();
                        packet = Packet.getNewPacket(MinecraftServer.getServer().getLogAgent(), packetId);
                        if (packet == null) {
                            throw new IOException("Bad packet id " + packetId);
                        }
                        checkpoint(ReadState.DATA);
                    case DATA:
                        packet.readPacketData(input);
                        checkpoint(ReadState.HEADER);
                        out.add(packet);
                        if (packet instanceof Packet254ServerPing) {
                            shutdown = true;
                            return;
                        }
                        packet = null;
                        break;
                    default:
                        throw new IllegalStateException();
                }
            }
        } catch (EOFException ex) {
        }
    }
}