package jk_5.nailed.util

import net.minecraft.server.MinecraftServer
import net.minecraft.src.{Packet, ChatMessageComponent}

/**
 * No description given
 *
 * @author jk-5
 */
object ServerUtils {
  def broadcastChatMessage(msg: String) = MinecraftServer.getServer.getConfigurationManager.sendChatMsg(ChatMessageComponent.func_111066_d(msg))
  def broadcastChatMessage(p: Packet) = MinecraftServer.getServer.getConfigurationManager.sendPacketToAllPlayers(p)
}
