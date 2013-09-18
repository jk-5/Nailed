package jk_5.nailed.network;

import io.netty.channel.*;
import io.netty.handler.timeout.ReadTimeoutHandler;
import jk_5.nailed.network.codec.PacketDecoder;
import jk_5.nailed.network.codec.PacketEncoder;

/**
 * No description given
 *
 * @author jk-5
 */
public class Pipeline extends ChannelInitializer {

    @Override
    public void initChannel(Channel ch) throws Exception {
        try{
            ch.config().setOption(ChannelOption.IP_TOS, 0x18);
        }catch(ChannelException ex){
            //NOOP
        }
        try{
            ch.config().setOption(ChannelOption.TCP_NODELAY, false);
        }catch(ChannelException ex){
            //NOOP
        }

        final ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("timer", new ReadTimeoutHandler(30));
        pipeline.addLast("decoder", new PacketDecoder());
        pipeline.addLast("encoder", new PacketEncoder());
        pipeline.addLast("manager", new NettyNetworkManager());
    }
}
