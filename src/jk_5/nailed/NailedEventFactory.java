package jk_5.nailed;

import jk_5.nailed.event.player.PlayerChatEvent;
import jk_5.nailed.event.player.PlayerJoinServerEvent;
import jk_5.nailed.event.player.PlayerLeaveServerEvent;
import jk_5.nailed.map.Mappack;
import jk_5.nailed.players.Player;
import net.minecraft.src.*;

/**
 * No description given
 *
 * @author jk-5
 */
public class NailedEventFactory {

    public static void playerLoggedIn(EntityPlayerMP entity, WorldServer world) {
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

    public static ChunkCoordinates getSpawnPoint(World world) {
        Mappack map = Nailed.mapLoader.getMapFromWorld((WorldServer) world).getMappack();
        return map.getSpawnPoint();
    }

    public static ChunkCoordinates getSpawnPoint(EntityPlayerMP playerEntity) {
        Player player = Nailed.playerRegistry.getPlayer(playerEntity.getCommandSenderName());
        ChunkCoordinates spawn = getSpawnPoint(playerEntity.getServerForPlayer());
        if(player != null && player.getTeam() != null && player.getTeam().shouldOverrideSpawnpoint()){
            spawn = player.getTeam().getSpawnpoint();
        }
        return spawn;
    }

    public static boolean canPlayerPickup(EntityPlayer entity) {
        return !Nailed.playerRegistry.getPlayer(entity.getCommandSenderName()).isSpectator();
    }

    public static boolean canPlayerAttackPlayer(EntityPlayerMP attacker, EntityPlayer dest){
        Player player = Nailed.playerRegistry.getPlayer(attacker.getCommandSenderName());
        if(player == null) return false;
        return !player.isSpectator();
    }

    public static void flushWorlds() {

    }

    public static void saveWorlds() {

    }
}
