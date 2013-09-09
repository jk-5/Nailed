package jk_5.nailed.map;

import com.beust.jcommander.internal.Lists;
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
        for (Player player : this.getPlayers()) {
            player.sendChatMessage(EnumColor.GREEN + "[" + this.mappack.getMapName() + "]" + EnumColor.RESET + " " + message);
        }
    }

    public void travelPlayerToMap(Player player) {
        EntityPlayerMP entity = player.getEntity();
        ServerConfigurationManager confManager = Nailed.server.getConfigurationManager();
        confManager.recreatePlayerEntity(entity, 0, true, this);
        /*entity.getServerForPlayer().getEntityTracker().removePlayerFromTrackers(entity);
        entity.getServerForPlayer().getPlayerManager().removePlayer(entity);
        confManager.playerEntityList.remove(entity);

        player.getCurrentMap().getWorld().removeEntity(entity);





        EntityPlayerMP newPlayer = new EntityPlayerMP(Nailed.server, Nailed.server.worldServerForDimension(entity.dimension), entity.getCommandSenderName(), new ItemInWorldManager(player.getCurrentMap().getWorld()));
        newPlayer.playerNetServerHandler = entity.playerNetServerHandler;
        newPlayer.clonePlayer(entity, true); //TODO: maybe false
        newPlayer.entityId = entity.entityId;

        WorldServer worldServer = this.world;
        ChunkCoordinates spawn = this.getMappack().getSpawnPoint();
        newPlayer.setLocationAndAngles(spawn.posX, spawn.posY, spawn.posZ, 0F, 0F);

        worldServer.theChunkProviderServer.loadChunk((int) newPlayer.posX >> 4, (int) newPlayer.posZ >> 4);

        while(!worldServer.getCollidingBoundingBoxes(newPlayer, newPlayer.boundingBox).isEmpty()) {
            newPlayer.setPosition(newPlayer.posX, newPlayer.posY + 1.0D, newPlayer.posZ);
        }

        newPlayer.playerNetServerHandler.sendPacket(new Packet9Respawn(-1, (byte) this.getMappack().getDifficulty(), WorldType.DEFAULT, 256, entity.theItemInWorldManager.getGameType()));
        newPlayer.playerNetServerHandler.sendPacket(new Packet9Respawn(0, (byte) this.getMappack().getDifficulty(), WorldType.DEFAULT, 256, entity.theItemInWorldManager.getGameType()));
        newPlayer.isDead = true;

        newPlayer.playerNetServerHandler.sendPacket(new Packet9Respawn(newPlayer.dimension, (byte)newPlayer.worldObj.difficultySetting, newPlayer.worldObj.getWorldInfo().getTerrainType(), newPlayer.worldObj.getHeight(), newPlayer.theItemInWorldManager.getGameType()));
        var9 = var8.getSpawnPoint();
        newPlayer.playerNetServerHandler.setPlayerLocation(newPlayer.posX, newPlayer.posY, newPlayer.posZ, newPlayer.rotationYaw, newPlayer.rotationPitch);
        newPlayer.playerNetServerHandler.sendPacket(new Packet6SpawnPosition(var9.posX, var9.posY, var9.posZ));
        newPlayer.playerNetServerHandler.sendPacket(new Packet43Experience(newPlayer.experience, newPlayer.experienceTotal, newPlayer.experienceLevel));
        this.updateTimeAndWeatherForPlayer(newPlayer, var8);
        var8.getPlayerManager().addPlayer(newPlayer);
        var8.spawnEntityInWorld(newPlayer);
        this.playerEntityList.add(newPlayer);
        newPlayer.addSelfToInternalCraftingInventory();
        newPlayer.setEntityHealth(newPlayer.func_110143_aJ());
        return newPlayer;*/







        /*Packet9Respawn packet = new Packet9Respawn();
        packet.difficulty = this.getMappack().getDifficulty();
        packet.gameType = this.getMappack().getDefaultGamemode();
        packet.respawnDimension = -1;
        packet.terrainType = WorldType.DEFAULT;
        packet.worldHeight = 256;
        player.sendPacket(packet);
        packet.respawnDimension = 0;
        player.sendPacket(packet);*/

        /*WorldServer current = player.getCurrentMap().getWorld();
        WorldServer destination = this.world;

        current.removeEntity(entity);
        entity.isDead = false;
        Nailed.server.getConfigurationManager().transferEntityToWorld(entity, entity.dimension, current, destination);
        Entity newEntity = EntityList.createEntityByName(EntityList.getEntityString(entity), destination);
        if(newEntity != null) {
            newEntity.copyDataFrom(entity, true);
            ChunkCoordinates var7 = destination.getSpawnPoint();
            var7.posY = entity.worldObj.getTopSolidOrLiquidBlock(var7.posX, var7.posZ);
            newEntity.setLocationAndAngles((double)var7.posX, (double)var7.posY, (double)var7.posZ, newEntity.rotationYaw, newEntity.rotationPitch);
            destination.spawnEntityInWorld(newEntity);
        }

        player.setCurrentMap(this);
        entity.isDead = true;
        current.resetUpdateEntityTick();
        destination.resetUpdateEntityTick();

        player.sendPacket(new Packet9Respawn(-1, (byte) destination.difficultySetting, WorldType.FLAT, 256, destination.getWorldInfo().getGameType()));
        player.sendPacket(new Packet9Respawn(0, (byte) destination.difficultySetting, WorldType.FLAT, 256, destination.getWorldInfo().getGameType()));
        player.sendPacket(new Packet6SpawnPosition(destination.getSpawnPoint().posX, destination.getSpawnPoint().posY, destination.getSpawnPoint().posZ));
        */
    }
}
