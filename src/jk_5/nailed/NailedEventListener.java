package jk_5.nailed;

import com.google.common.eventbus.Subscribe;
import jk_5.nailed.event.player.PlayerChatEvent;
import jk_5.nailed.event.player.PlayerJoinServerEvent;
import jk_5.nailed.event.player.PlayerLeaveServerEvent;
import jk_5.nailed.teams.Team;
import jk_5.nailed.teams.TeamRegistry;
import jk_5.nailed.util.EnumColor;
import jk_5.nailed.util.ServerUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ChatMessageComponent;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class NailedEventListener {

    @Subscribe
    public void onPlayerJoinServer(PlayerJoinServerEvent event){
        ServerUtils.broadcastChatMessage(event.player.formatChatMessage(EnumColor.YELLOW + "joined the game!"));
    }

    @Subscribe
    public void onPlayerLeaveServer(PlayerLeaveServerEvent event){
        ServerUtils.broadcastChatMessage(event.player.formatChatMessage(EnumColor.YELLOW + "left the game!"));
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event){
        ServerUtils.broadcastChatMessage(event.player.formatChatMessage(event.message));
    }
}
