package jk_5.nailed.irc.lib

import io.netty.channel.{SimpleChannelInboundHandler, ChannelHandlerContext}
import jk_5.nailed.Version

/**
 * No description given
 *
 * @author jk-5
 */
class IrcHandler(private final val bot: IrcBot) extends SimpleChannelInboundHandler[String] {

  private var stage = 0
  private var listener: Option[IrcListener] = None
  private var nickAttempts = 0

  @inline def setListener(listener: IrcListener) = this.listener = Option(listener)

  override def channelActive(ctx: ChannelHandlerContext){
    if(this.bot.serverPass != null && !this.bot.serverPass.isEmpty) ctx.writeAndFlush("PASS " + this.bot.serverPass)
    ctx.writeAndFlush("NICK " + this.bot.name)
    ctx.writeAndFlush("USER Nailed 8 * :" + Version.version)
    this.stage = 1
  }

  def channelRead0(ctx: ChannelHandlerContext, msg: String){
    val data = msg.split(" ", 4)
    val opcode = data(1)
    opcode match{
      case "004" => {
        stage = 2
        this.callListener(_.onConnect())
      } //Connected!
    }
    println(msg)
  }

  private def callListener(f:(IrcListener) => Unit) = this.listener.foreach(f)

  override def channelReadComplete(ctx: ChannelHandlerContext) = ctx.flush()

  override def exceptionCaught(ctx: ChannelHandlerContext, t: Throwable){
    ctx.close()
  }
}
