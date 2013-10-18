package jk_5.nailed.network.codec

import io.netty.handler.codec.MessageToMessageDecoder
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import javax.crypto.Cipher
import java.util
import jk_5.nailed.network.CipherBase

/**
 * No description given
 *
 * @author jk-5
 */
class CipherDecoder(private final val ci: CipherBase) extends MessageToMessageDecoder[ByteBuf] {
  def this(cipher: Cipher) = this(new CipherBase(cipher))
  override def decode(ctx: ChannelHandlerContext, msg: ByteBuf, out: util.List[AnyRef]) = out.add(this.ci.cipher(ctx, msg))
}
