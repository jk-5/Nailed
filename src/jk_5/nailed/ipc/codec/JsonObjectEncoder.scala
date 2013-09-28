package jk_5.nailed.ipc.codec

import io.netty.handler.codec.MessageToMessageEncoder
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.netty.channel.ChannelHandlerContext
import com.nexus.data.json.JsonObject
import java.util

/**
 * No description given
 *
 * @author jk-5
 */
class JsonObjectEncoder extends MessageToMessageEncoder[JsonObject] {

  override def encode(ctx: ChannelHandlerContext, data: JsonObject, out: util.List[AnyRef]){
    out.add(new TextWebSocketFrame(data.stringify))
  }
}
