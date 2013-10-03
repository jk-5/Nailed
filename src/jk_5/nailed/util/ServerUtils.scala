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

object Utils{
  def formatSeconds(seconds: Int): String = {
    if (seconds < 60) return "%d second%s".format(seconds, if (seconds == 1) "" else "s")
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    val builder = new StringBuilder
    var hasText = false
    if (hours > 0) {
      builder.append(hours)
      builder.append(" hour")
      if (hours > 1) builder.append("s")
      hasText = true
    }
    if (minutes > 0) {
      if (hasText) if (secs > 0) builder.append(", ")
      else builder.append(" and ")
      builder.append(minutes)
      builder.append(" minute")
      if (minutes > 1) builder.append("s")
      hasText = true
    }
    if (secs > 0) {
      if (hasText) builder.append(" and ")
      builder.append(secs)
      builder.append(" second")
      if (secs > 1) builder.append("s")
      hasText = true
    }
    if (!hasText) builder.append("0 seconds")
    builder.toString()
  }
}