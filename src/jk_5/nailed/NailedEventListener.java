package jk_5.nailed;

import com.google.common.eventbus.Subscribe;
import jk_5.nailed.event.player.PlayerChatEvent;
import jk_5.nailed.event.player.PlayerJoinServerEvent;
import jk_5.nailed.event.player.PlayerLeaveServerEvent;
import jk_5.nailed.network.packet.Packet21PlayerJoin;
import jk_5.nailed.network.packet.Packet22PlayerLeave;
import jk_5.nailed.util.EnumColor;
import jk_5.nailed.util.ServerUtils;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class NailedEventListener {

    @Subscribe
    public void onPlayerJoinServer(PlayerJoinServerEvent event) {
        ServerUtils.broadcastChatMessage(event.player.formatChatMessage(EnumColor.YELLOW + "joined the game").replace("[", "").replace("]", ""));
        Nailed.ipc.sendPacket(new Packet21PlayerJoin(event.player.getUsername()));
    }

    @Subscribe
    public void onPlayerLeaveServer(PlayerLeaveServerEvent event) {
        ServerUtils.broadcastChatMessage(event.player.formatChatMessage(EnumColor.YELLOW + "left the game").replace("[", "").replace("]", ""));
        Nailed.ipc.sendPacket(new Packet22PlayerLeave(event.player.getUsername()));
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        ServerUtils.broadcastChatMessage(event.player.formatChatMessage(event.message));
    }
}
