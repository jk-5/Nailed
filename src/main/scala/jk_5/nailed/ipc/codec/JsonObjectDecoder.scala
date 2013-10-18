package jk_5.nailed.ipc.codec

import io.netty.handler.codec.MessageToMessageDecoder
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.netty.channel.ChannelHandlerContext
import java.util
import com.nexus.data.json.JsonObject

/**
 * No description given
 *
 * @author jk-5
 */
class JsonObjectDecoder extends MessageToMessageDecoder[TextWebSocketFrame] {

  override def decode(ctx: ChannelHandlerContext, msg: TextWebSocketFrame, out: util.List[AnyRef]){
    try{
      out.add(JsonObject.readFrom(msg.text()))
    }catch{
      case e: Exception => out.add(msg)
    }
  }
}
