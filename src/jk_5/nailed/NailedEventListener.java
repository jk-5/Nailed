package jk_5.nailed;

import com.google.common.eventbus.Subscribe;
import jk_5.nailed.event.player.PlayerChatEvent;
import jk_5.nailed.event.player.PlayerJoinServerEvent;
import jk_5.nailed.event.player.PlayerLeaveServerEvent;
import jk_5.nailed.teams.Team;
import jk_5.nailed.teams.TeamRegistry;
import jk_5.nailed.util.EnumColor;
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
        Team playerTeam = Nailed.teamRegistry.getTeamFromPlayer(event.player.getCommandSenderName());
        String format = String.format("%s%s (%s)%s joined the game!", playerTeam.getColor().toString(), event.player.getCommandSenderName(), playerTeam.getName(), EnumColor.YELLOW);
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(ChatMessageComponent.func_111066_d(format));
    }

    @Subscribe
    public void onPlayerLeaveServer(PlayerLeaveServerEvent event){
        Team playerTeam = Nailed.teamRegistry.getTeamFromPlayer(event.player.getCommandSenderName());
        String format = String.format("%s%s (%s)%s left the game!", playerTeam.getColor().toString(), event.player.getCommandSenderName(), playerTeam.getName(), EnumColor.YELLOW);
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(ChatMessageComponent.func_111066_d(format));
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event){
        Team playerTeam = Nailed.teamRegistry.getTeamFromPlayer(event.player.getCommandSenderName());
        String format = String.format("%s%s<%s> %s", playerTeam == Team.UNKNOWN ? "" : playerTeam.getColor().toString() + "* " + playerTeam.getName() + " " + EnumColor.RESET, EnumColor.GREY, event.player.getCommandSenderName(), event.message);
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(ChatMessageComponent.func_111066_d(format));
    }
}
