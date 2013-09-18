package jk_5.nailed.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import jk_5.nailed.network.CipherBase;

import javax.crypto.Cipher;
import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public class CipherDecoder extends MessageToMessageDecoder<ByteBuf> {

    private final CipherBase cipher;

    public CipherDecoder(Cipher cipher){
        this.cipher = new CipherBase(cipher);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception{
        out.add(cipher.cipher(ctx, msg));
    }
}
