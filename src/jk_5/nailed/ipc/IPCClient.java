package jk_5.nailed.ipc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import jk_5.nailed.ipc.packet.Packet;

import java.net.ConnectException;
import java.net.URI;

/**
 * No description given
 *
 * @author jk-5
 */
public class IPCClient extends Thread {

    private URI uri;
    private Channel channel;

    public IPCClient() {
        try {
            this.uri = new URI("ws://krypton.local:5000/minecraft");
        } catch (Exception e) {
            this.uri = null;
        }
        this.setDaemon(true);
        this.setName("IPC Client thread");
    }

    public void sendPacket(Packet p) {
        try {
            if (this.channel != null) this.channel.writeAndFlush(new TextWebSocketFrame(p.getSendPacket().toString()));
        } catch (Exception e) {
            //NOOP
        }
    }

    public void disconnect() {
        this.channel.close().syncUninterruptibly();
    }

    public void reconnect() {
        this.disconnect();
        this.start();
    }

    public URI getURI() {
        return this.uri;
    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            final IPCHandler handler = new IPCHandler();
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("http-codec", new HttpClientCodec());
                            ch.pipeline().addLast("aggregator", new HttpObjectAggregator(8192));
                            ch.pipeline().addLast("ws-handler", handler);
                        }
                    });
            this.channel = b.connect(uri.getHost(), uri.getPort()).sync().channel();
            handler.handshakeFuture().sync();
            this.channel.closeFuture().sync();
        } catch (Exception e) {
            if (e instanceof ConnectException) System.out.println("Was not able to connect to IPC server");
            else {
                System.err.println("IPC Error:");
                e.printStackTrace();
                System.exit(1);
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
