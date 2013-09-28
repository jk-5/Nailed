package jk_5.nailed.ipc

import java.net.{ConnectException, URI}
import io.netty.channel.{EventLoopGroup, Channel}
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.bootstrap.Bootstrap
import io.netty.channel.socket.nio.NioSocketChannel
import jk_5.nailed.ipc.packet.IPCPacket

/**
 * No description given
 *
 * @author jk-5
 */
class IPCClient extends Thread {

  this.setDaemon(true)
  this.setName("IPC Client thread")

  private final val uri: URI = new URI("ws://minecraft.reening.nl:5000/server")
  private var channel: Channel = null

  @inline def sendPacket(p: IPCPacket) = this.channel.writeAndFlush(p)
  @inline def disconnect() = if(this.channel.isActive && this.channel.isOpen) this.channel.close()
  @inline def reconnect() = {this.disconnect(); this.start()}
  @inline def getURI = this.uri

  override def run(){
    val group: EventLoopGroup = new NioEventLoopGroup
    try{
      val handler = new IPCHandler
      val b = new Bootstrap
      b.group(group).channel(classOf[NioSocketChannel])
      b.handler(new Pipeline(handler))
      this.channel = b.connect(uri.getHost, uri.getPort).sync.channel()
      handler.getHandshakeFuture.sync
      this.channel.closeFuture.sync
    }catch{
      case e: Exception => {
        if (e.isInstanceOf[ConnectException]) println("Was not able to connect to IPC server")
        else{
          System.err.println("IPC Error:")
          e.printStackTrace()
          sys.exit(1)
        }
      }
    }finally{
      group.shutdownGracefully
    }
  }
}
