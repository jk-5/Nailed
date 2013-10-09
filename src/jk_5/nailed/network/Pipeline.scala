package jk_5.nailed.network

import io.netty.channel.{ChannelOption, ChannelInitializer}
import io.netty.channel.socket.SocketChannel
import io.netty.handler.timeout.ReadTimeoutHandler
import jk_5.nailed.network.codec.{PacketDecoder, PacketEncoder}

/**
 * No description given
 *
 * @author jk-5
 */
object Pipeline extends ChannelInitializer[SocketChannel] {

  def initChannel(ch: SocketChannel){
    try{ch.config().setOption(ChannelOption.IP_TOS.asInstanceOf[ChannelOption[Any]], 0x18)}
    try{ch.config().setOption(ChannelOption.TCP_NODELAY.asInstanceOf[ChannelOption[Any]], false)}
    val pipeline = ch.pipeline()
    pipeline.addLast("timer", new ReadTimeoutHandler(30))
    pipeline.addLast("decoder", new PacketDecoder)
    pipeline.addLast("encoder", PacketEncoder)
    pipeline.addLast("manager", new NettyNetworkManager)
  }
}
