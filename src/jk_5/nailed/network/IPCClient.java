package jk_5.nailed.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class IPCClient extends Thread {

    private final String host;
    private final int port;

    public IPCClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.setDaemon(true);
        this.setName("IPC Client thread");
    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new IPCHandler());
                        }
                    });
            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            System.err.println("IPC Error:");
            e.printStackTrace();
            //System.exit(1);
        } finally {
            group.shutdownGracefully();
        }
    }
}
