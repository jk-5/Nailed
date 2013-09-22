package jk_5.nailed.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import jk_5.nailed.network.CipherBase;

import javax.crypto.Cipher;
import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public class CipherEncoder extends MessageToByteEncoder<ByteBuf> {

    private final CipherBase cipher;

    public CipherEncoder(Cipher cipher){
        this.cipher = new CipherBase(cipher);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception{
        cipher.cipher(in, out);
    }
}
