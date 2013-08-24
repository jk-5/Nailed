package jk_5.nailed.network.packet;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Map;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public abstract class Packet {

    private static BiMap<Integer, Class<? extends Packet>> packetMap = HashBiMap.create();
    private static final int magic1 = 0x55;
    private static final int magic2 = 0xAA;

    static {
        packetMap.put(0x01, PacketSYN.class);
    }

    public abstract void writeToBuffer(ByteBuf buffer);
    public abstract void readFromBuffer(ByteBuf buffer);

    public abstract int getSize();

    public int getPacketID(){
        return packetMap.inverse().get(this.getClass());
    }

    public ByteBuf getSendBuffer(){
        ByteBuf buf = Unpooled.buffer(this.getSize() + 3);
        buf.writeByte(magic1);
        buf.writeByte(magic2);
        buf.writeByte(this.getPacketID());
    }
}
