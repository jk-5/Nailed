package jk_5.nailed.ipc

import jk_5.nailed.ipc.packet.{IPCPacket, PacketInitConnection}
import io.netty.channel._
import io.netty.handler.codec.http.websocketx._
import jk_5.nailed.Nailed
import io.netty.handler.codec.http.{FullHttpResponse, DefaultHttpHeaders}

/**
 * No description given
 *
 * @author jk-5
 */
class IPCHandler extends SimpleChannelInboundHandler[AnyRef] {

  private final val handshaker: WebSocketClientHandshaker = WebSocketClientHandshakerFactory.newHandshaker(Nailed.ipc.getURI, WebSocketVersion.V13, null, false, new DefaultHttpHeaders)
  private var handshakeFuture: ChannelPromise = null

  @inline def getHandshakeFuture = this.handshakeFuture
  @inline override def handlerAdded(ctx: ChannelHandlerContext) = this.handshakeFuture = ctx.newPromise
  @inline override def channelActive(ctx: ChannelHandlerContext) = this.handshaker.handshake(ctx.channel)
  @inline override def channelInactive(ctx: ChannelHandlerContext) = println("WebSocket Client disconnected!")

  def channelRead0(ctx: ChannelHandlerContext, msg: AnyRef) {
    if (!handshaker.isHandshakeComplete) {
      handshaker.finishHandshake(ctx.channel(), msg.asInstanceOf[FullHttpResponse])
      System.out.println("WebSocket Client connected!")
      handshakeFuture.setSuccess()
      ctx.writeAndFlush(new PacketInitConnection)
      return
    }
    msg match{
      case packet: IPCPacket => packet.process()
      case close: CloseWebSocketFrame => ctx.close()
      case pong: PongWebSocketFrame => {}
      case p => throw new Exception("Unexpected data: " + p.getClass.getSimpleName)
    }
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
    cause.printStackTrace()
    if (!handshakeFuture.isDone) handshakeFuture.setFailure(cause)
    ctx.close
  }
}
