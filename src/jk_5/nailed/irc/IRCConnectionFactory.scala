package jk_5.nailed.irc

import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.bootstrap.Bootstrap
import java.net.InetSocketAddress
import io.netty.channel.socket.nio.NioSocketChannel

/**
 * No description given
 *
 * @author jk-5
 */
object IRCConnectionFactory {

  def connect(server: InetSocketAddress){
    val group: EventLoopGroup = new NioEventLoopGroup
    val b = new Bootstrap().group(group).channel(classOf[NioSocketChannel]).handler(Pipeline)
  }
}
