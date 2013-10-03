package jk_5.nailed.irc

import io.netty.channel.ChannelInitializer
import io.netty.handler.codec.string.{StringEncoder, StringDecoder}
import io.netty.util.CharsetUtil
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.{Delimiters, DelimiterBasedFrameDecoder}

/**
 * No description given
 *
 * @author jk-5
 */
object Pipeline extends ChannelInitializer[SocketChannel] {

  def initChannel(ch: SocketChannel){
    ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter(): _*))
    ch.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8))
    ch.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8))
  }
}
