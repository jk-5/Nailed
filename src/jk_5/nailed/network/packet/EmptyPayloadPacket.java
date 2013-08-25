package jk_5.nailed.network.packet;

import io.netty.buffer.ByteBuf;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public abstract class EmptyPayloadPacket extends Packet {

    @Override
    public final void writeToBuffer(ByteBuf buffer) {

    }

    @Override
    public final void readFromBuffer(ByteBuf buffer) {

    }

    @Override
    public final int getSize() {
        return 0;
    }
}
