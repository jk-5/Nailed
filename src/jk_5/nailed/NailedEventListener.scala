package jk_5.nailed

import jk_5.nailed.util.{EnumColor, ServerUtils}
import jk_5.nailed.ipc.packet.{PacketPlayerLeave, PacketPlayerJoin}
import jk_5.nailed.team.Team
import com.google.common.eventbus.Subscribe
import jk_5.nailed.event.{PlayerChatEvent, PlayerLeaveServerEvent, PlayerJoinServerEvent}
import jk_5.nailed.ipc.IPCClient

/**
 * No description given
 *
 * @author jk-5
 */
object NailedEventListener {
  @Subscribe def onPlayerJoinServer(event: PlayerJoinServerEvent){
    event.player.onLogin()
    ServerUtils.broadcastChatMessage(event.player.formatChatMessage(EnumColor.YELLOW + "joined the game").replace("[", "").replace("]", ""))
    IPCClient.sendPacket(new PacketPlayerJoin(event.player.getUsername))
  }

  @Subscribe def onPlayerLeaveServer(event: PlayerLeaveServerEvent){
    ServerUtils.broadcastChatMessage(event.player.formatChatMessage(EnumColor.YELLOW + "left the game").replace("[", "").replace("]", ""))
    IPCClient.sendPacket(new PacketPlayerLeave(event.player.getUsername))
  }

  @Subscribe def onPlayerChat(event: PlayerChatEvent){
    if (event.player.getTeam eq Team.UNKNOWN) {
      ServerUtils.broadcastChatMessage(event.player.formatChatMessage(event.message))
    }else {
      event.player.getTeam.sendChatMessage(event.player.formatChatMessage(event.message))
    }
  }
}
