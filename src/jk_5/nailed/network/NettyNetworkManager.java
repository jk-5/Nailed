package jk_5.nailed.network;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import jk_5.nailed.network.codec.CipherDecoder;
import jk_5.nailed.network.codec.CipherEncoder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.PrivateKey;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * No description given
 *
 * @author jk-5
 */
public class NettyNetworkManager extends SimpleChannelInboundHandler<Packet> implements INetworkManager {

    private static final ExecutorService threadPool = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("Async Packet Handler - %1$d").build());
    private static final MinecraftServer server = MinecraftServer.getServer();
    private static final PrivateKey key = server.getKeyPair().getPrivate();
    private static final NettyServerHandler serverConnection = (NettyServerHandler) server.getNetworkThread();
    private final Queue<Packet> syncPackets = new ConcurrentLinkedQueue<Packet>();
    private volatile boolean connected;
    private ChannelHandlerContext ctx;
    private SocketAddress address;
    NetHandler connection;
    private SecretKey secret;
    private String dcReason;
    private Object[] dcArgs;
    private Socket socketAdaptor;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        address = ctx.channel().remoteAddress();
        socketAdaptor = NettySocketAdaptor.adapt((SocketChannel) ctx.channel());
        connection = new NetLoginHandler(server, this);
        connected = true;
        serverConnection.register((NetLoginHandler) connection);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.networkShutdown("disconnect.endOfStream");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        this.networkShutdown("disconnect.genericReason", "Netty error. See log for details");
        cause.printStackTrace();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, final Packet msg) throws Exception {
        if (connected) {
            if (msg instanceof Packet252SharedKey) {
                secret = ((Packet252SharedKey) msg).getSharedKey(key);
                Cipher decrypt = NettyServerHandler.getCipher(Cipher.DECRYPT_MODE, secret);
                ctx.pipeline().addBefore("decoder", "decrypt", new CipherDecoder(decrypt));
            }

            if (msg.canProcessAsync()) {
                threadPool.submit(new Runnable() {
                    public void run() {
                        msg.processPacket(connection);
                    }
                });
            } else {
                syncPackets.add(msg);
            }
        }
    }

    public Socket getSocket() {
        return socketAdaptor;
    }

    public void setNetHandler(NetHandler nh) {
        connection = nh;
    }

    public void addToSendQueue(final Packet packet) {
        if (connected) {
            if (ctx.channel().eventLoop().inEventLoop()) {
                queue0(packet);
            } else {
                ctx.channel().eventLoop().execute(new Runnable() {
                    public void run() {
                        queue0(packet);
                    }
                });
            }
        }
    }

    private void queue0(Packet packet) {
        ctx.writeAndFlush(packet);
        if (packet instanceof Packet252SharedKey) {
            Cipher encrypt = NettyServerHandler.getCipher(Cipher.ENCRYPT_MODE, secret);
            ctx.pipeline().addBefore("decoder", "encrypt", new CipherEncoder(encrypt));
        }
    }

    public void wakeThreads() {

    }

    public void processReadPackets() {
        for (int i = 1000; !syncPackets.isEmpty() && i >= 0; i--) {
            if (connection instanceof NetLoginHandler ? ((NetLoginHandler) connection).finishedProcessing : ((NetServerHandler) connection).connectionClosed) {
                syncPackets.clear();
                break;
            }
            syncPackets.poll().processPacket(connection);
        }
        if (!connected && (dcReason != null || dcArgs != null)) {
            connection.handleErrorMessage(dcReason, dcArgs);
        }
    }

    public SocketAddress getRemoteAddress() {
        return address;
    }

    public void serverShutdown() {
        if (connected) {
            connected = false;
            ctx.close();
        }
    }

    public int getNumChunkDataPackets() {
        return 0;
    }

    public void networkShutdown(String reason, Object... arguments) {
        if (connected) {
            dcReason = reason;
            dcArgs = arguments;
            this.serverShutdown();
        }
    }
}