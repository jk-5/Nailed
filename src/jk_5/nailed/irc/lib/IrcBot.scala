package jk_5.nailed.irc.lib

import io.netty.bootstrap.Bootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.{ChannelInitializer, Channel, ChannelFuture}
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.{Delimiters, DelimiterBasedFrameDecoder}
import io.netty.handler.codec.string.{StringEncoder, StringDecoder}
import io.netty.util.CharsetUtil

/**
 * No description given
 *
 * @author jk-5
 */
class IrcBot(private [lib] final val name: String) extends Runnable {

  private var channel: Channel = _
  private var hostname: String = null
  private var port = 6667
  private [lib] var serverPass: String = null
  private final val thread = new Thread(this)
  private final val handler = new IrcHandler(this)

  def connect(hostname: String, port: Int = 6667, serverPass: String = null){
    this.hostname = hostname
    this.port = port
    this.serverPass = serverPass
    this.thread.start()
  }

  def run(){
    val group = new NioEventLoopGroup
    val b = new Bootstrap().group(group).channel(classOf[NioSocketChannel]).handler(new ChannelInitializer[SocketChannel]{
      def initChannel(ch: SocketChannel){
        ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter(): _*))
        ch.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8))
        ch.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8))
        ch.pipeline().addLast("lineSep", LineSeperatorInsertor)
        ch.pipeline().addLast("handler", handler)
      }
    })
    try{
      val future = b.connect(this.hostname, this.port).sync()
      this.channel = future.channel()
      if(!future.isSuccess) close
      this.channel.closeFuture().sync()
    }finally{
      group.shutdownGracefully()
    }
  }

  def close: ChannelFuture = this.channel.close()
}
