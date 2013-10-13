package jk_5.nailed.ipc

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.{HttpObjectAggregator, HttpClientCodec}
import jk_5.nailed.ipc.codec.{PacketEncoder, PacketDecoder, JsonObjectEncoder, JsonObjectDecoder}

/**
 * No description given
 *
 * @author jk-5
 */
class Pipeline(private final val handler: IPCHandler) extends ChannelInitializer[SocketChannel] {

  def initChannel(ch: SocketChannel) {
    ch.pipeline.addLast("http-codec", new HttpClientCodec)
    ch.pipeline.addLast("http-aggregator", new HttpObjectAggregator(65536))
    ch.pipeline.addLast("jsonDecoder", new JsonObjectDecoder)
    ch.pipeline.addLast("jsonEncoder", new JsonObjectEncoder)
    ch.pipeline.addLast("packetDecoder", new PacketDecoder)
    ch.pipeline.addLast("packetEncoder", new PacketEncoder)
    ch.pipeline.addLast("ws-handler", handler)
  }
}
