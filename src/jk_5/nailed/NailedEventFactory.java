package jk_5.nailed;

import jk_5.nailed.event.player.PlayerChatEvent;
import jk_5.nailed.event.player.PlayerJoinServerEvent;
import jk_5.nailed.event.player.PlayerLeaveServerEvent;
import jk_5.nailed.map.Mappack;
import jk_5.nailed.players.Player;
import jk_5.nailed.util.EnumColor;
import net.minecraft.src.*;

import java.net.InetAddress;
import java.util.Random;

/**
 * No description given
 *
 * @author jk-5
 */
public class NailedEventFactory {

    private static final String motd[] = new String[]{"Well, that escalated quickly!", "Let\'s go!", "Oh well...", "Hello world!", "That\'s me!", "Oh god why!?", "Oh i hate the teams!", "FUCK THIS SHIT!", "I hate you!", "Kill them all!", "Blow it up!", "Fix yo laggz bro!", "Where\'s the enderpearl?", "It\'s opensource!", "Gimme starfall!", EnumColor.RANDOM + "FUNKY SHIT!", "Now 99% bug-free!"};
    public static String mode = System.getProperty("nailed.mode", "play");

    public static void playerLoggedIn(EntityPlayerMP entity, WorldServer world) {
        Player p = Nailed.playerRegistry.getOrCreatePlayer(entity.getCommandSenderName());
        if(p.getUsername().equals("Clank26") || p.getUsername().equals("PostVillageCore")) p.setGroup(Nailed.groupRegistry.getGroup("admin"));
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

    public static boolean canPlayerAttackPlayer(EntityPlayerMP a, EntityPlayer d){
        Player attacker = Nailed.playerRegistry.getPlayer(a.getCommandSenderName());
        Player attacked = Nailed.playerRegistry.getPlayer(d.getCommandSenderName());
        if(attacker == null || attacked == null) return false;
        if(attacker.isSpectator()) return false;
        if(!attacked.getCurrentMap().getMappack().isPvpEnabled()) return false;
        if(!attacker.getCurrentMap().getMappack().isPvpEnabled()) return false;
        if(attacker.getTeam() == attacked.getTeam()){
            return attacker.getTeam().friendlyFireEnabled();
        }
        return true;
    }

    public static void flushWorlds() {

    }

    public static void saveWorlds() {

    }

    public static boolean isServerPublic(){
        return mode.equals("play");
    }

    public static boolean isServerPublicForAddress(InetAddress address){
        return address.getHostName().equalsIgnoreCase("localhost") || isServerPublic();
    }

    public static int getProtocolVersion(InetAddress address){
        return isServerPublicForAddress(address) ? 74 : 5;
    }

    public static String getMCVersionForList(InetAddress address){
        return isServerPublicForAddress(address) ? "1.6.2" : "Offline";
    }

    public static String getMotdForList(){
        if(mode.equals("dev")) return EnumColor.GREEN + "[Nailed]" + EnumColor.RESET + " Development Mode";
        else if(mode.equals("build")) return EnumColor.GREEN + "[Nailed]" + EnumColor.RESET + " Build Mode";
        else return EnumColor.GREEN + "[Nailed]" + EnumColor.RESET + " " + motd[new Random().nextInt(motd.length)];
    }
}
