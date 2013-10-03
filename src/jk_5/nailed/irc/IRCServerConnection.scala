package jk_5.nailed.irc

import io.netty.bootstrap.Bootstrap
import io.netty.channel.{Channel, EventLoopGroup}
import io.netty.channel.nio.NioEventLoopGroup
import jk_5.nailed.ipc.{Pipeline, IPCHandler}
import io.netty.channel.socket.nio.NioSocketChannel
import java.net.ConnectException

/**
 * No description given
 *
 * @author jk-5
 */
class IRCServerConnection(private final val server: String, private final val port: Int = 9997) {

  private final var channel: Channel = _


}
