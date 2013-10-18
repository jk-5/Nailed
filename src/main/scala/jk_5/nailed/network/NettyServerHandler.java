package jk_5.nailed.network;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.NetworkListenThread;
import net.minecraft.server.MinecraftServer;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.net.InetAddress;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.*;

/**
 * No description given
 *
 * @author jk-5
 */
public class NettyServerHandler extends NetworkListenThread {

    private final ChannelFuture socket;
    private static EventLoopGroup group;
    private final List<NetLoginHandler> pending = Collections.synchronizedList(new ArrayList<NetLoginHandler>());

    public NettyServerHandler(final MinecraftServer ms, InetAddress host, int port) throws IOException {
        super(ms);
        if (group == null) {
            group = new NioEventLoopGroup(4, new ThreadFactoryBuilder().setNameFormat("Netty IO Thread - %1$d").build());
        }

        socket = new ServerBootstrap().channel(NioServerSocketChannel.class).childHandler(Pipeline$.MODULE$).group(group).localAddress(host, port).bind().syncUninterruptibly();
    }

    @Override
    public void stopListening() {
        socket.channel().close().syncUninterruptibly();
    }

    @Override
    public void networkTick() {
        super.networkTick();
        for (int i = 0; i < pending.size(); ++i) {
            NetLoginHandler connection = pending.get(i);

            try {
                connection.tryLogin();
            } catch (Exception ex) {
                connection.raiseErrorAndDisconnect("Server-side network error, See log for details");
                System.err.println("Error while handling packet:");
                ex.printStackTrace();
            }

            if (connection.connectionComplete) {
                pending.remove(i--);
            }
        }
    }

    public void register(NetLoginHandler conn) {
        pending.add(conn);
    }

    public static Cipher getCipher(int opMode, Key key) {
        try {
            Cipher cip = Cipher.getInstance("AES/CFB8/NoPadding");
            cip.init(opMode, key, new IvParameterSpec(key.getEncoded()));
            return cip;
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }
    }
}