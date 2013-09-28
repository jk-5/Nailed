package jk_5.nailed;

import com.google.common.eventbus.Subscribe;
import jk_5.nailed.event.PlayerChatEvent;
import jk_5.nailed.event.PlayerJoinServerEvent;
import jk_5.nailed.event.PlayerLeaveServerEvent;
import jk_5.nailed.ipc.packet.PacketPlayerJoin;
import jk_5.nailed.ipc.packet.PacketPlayerLeave;
import jk_5.nailed.team.Team;
import jk_5.nailed.util.EnumColor;
import jk_5.nailed.util.ServerUtils;

/**
 * No description given
 *
 * @author jk-5
 */
public class NailedEventListener {

    @Subscribe
    public void onPlayerJoinServer(PlayerJoinServerEvent event) {
        event.player.onLogin();
        ServerUtils.broadcastChatMessage(event.player.formatChatMessage(EnumColor.YELLOW + "joined the game").replace("[", "").replace("]", ""));
        Nailed.ipc.sendPacket(new PacketPlayerJoin(event.player.getUsername()));
    }

    @Subscribe
    public void onPlayerLeaveServer(PlayerLeaveServerEvent event) {
        ServerUtils.broadcastChatMessage(event.player.formatChatMessage(EnumColor.YELLOW + "left the game").replace("[", "").replace("]", ""));
        Nailed.ipc.sendPacket(new PacketPlayerLeave(event.player.getUsername()));
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        if(event.player.getTeam() == Team.UNKNOWN){
            ServerUtils.broadcastChatMessage(event.player.formatChatMessage(event.message));
        }else{
            event.player.getTeam().sendChatMessage(event.player.formatChatMessage(event.message));
        }
    }
}
