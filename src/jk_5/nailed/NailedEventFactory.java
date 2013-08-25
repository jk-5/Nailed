package jk_5.nailed;

import jk_5.nailed.event.player.PlayerChatEvent;
import jk_5.nailed.event.player.PlayerJoinServerEvent;
import jk_5.nailed.event.player.PlayerLeaveServerEvent;
import jk_5.nailed.players.Player;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.EntityPlayerMP;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class NailedEventFactory {

    private static final int spawnX = Integer.parseInt(Nailed.mapManager.getConfig().getProperty("spawnpoint.x", "0"));
    private static final int spawnY = Integer.parseInt(Nailed.mapManager.getConfig().getProperty("spawnpoint.y", "64"));
    private static final int spawnZ = Integer.parseInt(Nailed.mapManager.getConfig().getProperty("spawnpoint.z", "0"));

    public static void playerLoggedIn(EntityPlayerMP entity) {
        Player p = Nailed.playerRegistry.getOrCreatePlayer(entity.getCommandSenderName());
        Nailed.eventBus.post(new PlayerJoinServerEvent(p));
    }

    public static void playerLoggedOut(EntityPlayerMP entity) {
        Player p = Nailed.playerRegistry.getOrCreatePlayer(entity.getCommandSenderName());
        Nailed.eventBus.post(new PlayerLeaveServerEvent(p));
    }

    public static void onPlayerChat(EntityPlayerMP entity, String message) {
        Player p = Nailed.playerRegistry.getOrCreatePlayer(entity.getCommandSenderName());
        Nailed.eventBus.post(new PlayerChatEvent(p, message));
    }

    public static boolean isOp(String username) {
        return Nailed.playerRegistry.getPlayer(username).getGroup().getGroupID().equalsIgnoreCase("admin");
    }

    public static ChunkCoordinates getSpawnPoint() {
        return new ChunkCoordinates(spawnX, spawnY, spawnZ);
    }
}
