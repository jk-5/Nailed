package jk_5.nailed.network.codec

import io.netty.handler.codec.MessageToByteEncoder
import io.netty.buffer.ByteBuf
import jk_5.nailed.network.CipherBase
import javax.crypto.Cipher
import io.netty.channel.ChannelHandlerContext
import java.util

/**
 * No description given
 *
 * @author jk-5
 */
class CipherEncoder(private final val ci: CipherBase) extends MessageToByteEncoder[ByteBuf] {
  def this(cipher: Cipher) = this(new CipherBase(cipher))
  override def encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf) = this.ci.cipher(msg, out)
}
