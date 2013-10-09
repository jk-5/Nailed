package jk_5.nailed.irc.lib

import io.netty.handler.codec.MessageToMessageEncoder
import io.netty.channel.ChannelHandlerContext
import java.util

/**
 * No description given
 *
 * @author jk-5
 */
object LineSeperatorInsertor extends MessageToMessageEncoder[String] {
  def encode(ctx: ChannelHandlerContext, msg: String, out: util.List[AnyRef]) = out.add(msg + "\r\n")
}
