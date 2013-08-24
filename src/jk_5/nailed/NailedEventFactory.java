package jk_5.nailed;

import jk_5.nailed.event.player.PlayerChatEvent;
import jk_5.nailed.event.player.PlayerJoinServerEvent;
import jk_5.nailed.event.player.PlayerLeaveServerEvent;
import jk_5.nailed.players.Player;
import net.minecraft.src.EntityPlayerMP;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class NailedEventFactory {

    public static void playerLoggedIn(EntityPlayerMP entity){
        Player p = Nailed.playerRegistry.getOrCreatePlayer(entity.getCommandSenderName());
        Nailed.eventBus.post(new PlayerJoinServerEvent(p));
    }

    public static void playerLoggedOut(EntityPlayerMP entity){
        Player p = Nailed.playerRegistry.getOrCreatePlayer(entity.getCommandSenderName());
        Nailed.eventBus.post(new PlayerLeaveServerEvent(p));
    }

    public static void onPlayerChat(EntityPlayerMP entity, String message){
        Player p = Nailed.playerRegistry.getOrCreatePlayer(entity.getCommandSenderName());
        Nailed.eventBus.post(new PlayerChatEvent(p, message));
    }

    public static boolean isOp(String username){
        return Nailed.playerRegistry.getPlayer(username).getGroup().getGroupID().equalsIgnoreCase("admin");
    }
}
