package jk_5.nailed.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ChatMessageComponent;
import net.minecraft.src.Packet;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class ServerUtils {

    public static void broadcastChatMessage(String message) {
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(ChatMessageComponent.func_111066_d(message));
    }

    public static void broadcastPacket(Packet packet) {
        MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(packet);
    }
}
