package jk_5.nailed.map;

import com.google.common.collect.Lists;
import jk_5.nailed.Nailed;
import jk_5.nailed.map.gameloop.GameThread;
import jk_5.nailed.players.Player;
import jk_5.nailed.util.EnumColor;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ServerConfigurationManager;
import net.minecraft.src.WorldServer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * No description given
 *
 * @author jk-5
 */
public class Map {

    private static final AtomicInteger nextId = new AtomicInteger(0);

    private final int UID = nextId.getAndIncrement();
    private WorldServer world;
    private final Mappack mappack;
    private final GameThread gameThread;
    private final TeamManager teamManager;

    public Map(Mappack mappack) {
        this.mappack = mappack;
        this.gameThread = new GameThread(this);
        this.teamManager = new TeamManager(this);
    }

    public WorldServer getWorld() {
        return this.world;
    }

    public int getUID() {
        return this.UID;
    }

    public Mappack getMappack() {
        return this.mappack;
    }

    public TeamManager getTeamManager() {
        return this.teamManager;
    }

    public String getFolderName() {
        return "map" + this.getUID() + this.getMappack().getInternalName();
    }

    public GameThread getGameThread() {
        return this.gameThread;
    }

    public void setWorldServer(WorldServer server) {
        this.world = server;
        this.teamManager.setupTeams();
    }

    public List<Player> getPlayers() {
        List<Player> players = Lists.newArrayList();
        for (Player p : Nailed.playerRegistry.getPlayers()) {
            if (p.getCurrentMap() == this) players.add(p);
        }
        return players;
    }

    public void sendMessageToAllPlayers(String message) {
        this.sendMessageToAllPlayersWithoutPrefix(EnumColor.GREEN + "[" + this.mappack.getMapName() + "]" + EnumColor.RESET + " " + message);
    }

    public void sendMessageToAllPlayersWithoutPrefix(String message){
        for (Player player : this.getPlayers()) {
            player.sendChatMessage(message);
        }
    }

    public void travelPlayerToMap(Player player) {
        EntityPlayerMP entity = player.getEntity();
        ServerConfigurationManager confManager = Nailed.server.getConfigurationManager();
        confManager.recreatePlayerEntity(entity, 0, true, this);
    }
}
