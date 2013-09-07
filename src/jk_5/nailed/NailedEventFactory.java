package jk_5.nailed;

import jk_5.nailed.event.player.PlayerChatEvent;
import jk_5.nailed.event.player.PlayerJoinServerEvent;
import jk_5.nailed.event.player.PlayerLeaveServerEvent;
import jk_5.nailed.players.Player;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.WorldServer;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class NailedEventFactory {

    private static final int spawnX = Nailed.mapManager.getConfig().getTag("spawnpoint").getTag("x").getIntValue(0);
    private static final int spawnY = Nailed.mapManager.getConfig().getTag("spawnpoint").getTag("y").getIntValue(64);
    private static final int spawnZ = Nailed.mapManager.getConfig().getTag("spawnpoint").getTag("z").getIntValue(0);

    public static void playerLoggedIn(EntityPlayerMP entity, WorldServer world) {
        Nailed.multiworldManager.onPlayerJoin(entity, world);
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

    public static boolean canPlayerPickup(EntityPlayer entity) {
        return !Nailed.playerRegistry.getPlayer(entity.getCommandSenderName()).isSpectator();
    }

    public static void flushWorlds() {

    }

    public static void saveWorlds() {

    }
}
