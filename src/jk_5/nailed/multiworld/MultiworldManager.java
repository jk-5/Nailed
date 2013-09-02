package jk_5.nailed.multiworld;

import com.google.common.collect.Maps;
import jk_5.nailed.Nailed;
import jk_5.nailed.NailedEventFactory;
import jk_5.nailed.util.FileUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class MultiworldManager {

    private File mapPack;
    private File mapsFolder = new File("maps");
    private Map<Integer, WorldServer> worlds = Maps.newHashMap();
    private Map<EntityPlayerMP, WorldServer> playerWorldMap = new WeakHashMap<EntityPlayerMP, WorldServer>();

    public void init() {
        this.mapPack = new File(Nailed.config.getTag("mappack").getTag("path").setComment("Path to the mappack file").getValue("mappack.zip"));
    }

    public void onPlayerJoin(EntityPlayerMP entity, WorldServer world) {
        this.playerWorldMap.put(entity, world);
    }

    public WorldServer createNewMapDimension(int id) {
        WorldServer overworld = MinecraftServer.getServer().worldServerForDimension(0);
        WorldSettings settings = new WorldSettings(overworld.getWorldInfo());
        WorldServer newWorld = new WorldServerMulti(MinecraftServer.getServer(), overworld.getSaveHandler(), this.unpackMap(id).getName(), 0, settings, overworld, MinecraftServer.getServer().theProfiler, overworld.getWorldLogAgent()); //FIXME: replace 0 with id?
        newWorld.addWorldAccess(new WorldManager(MinecraftServer.getServer(), newWorld));
        newWorld.getWorldInfo().setGameType(MinecraftServer.getServer().getGameType());
        //newWorld.getSaveHandler().
        MinecraftServer.getServer().setDifficultyForAllWorlds(MinecraftServer.getServer().getDifficulty());
        this.worlds.put(id, newWorld);
        return newWorld;
    }

    private File getMapFolderFromID(int id) {
        return new File(this.mapsFolder, "map" + id);
    }

    public void setDefaultMapID(int id) {
        File map = this.unpackMap(id);
        Nailed.server.setFolderName("maps" + System.getProperty("file.seperator", "/") + map.getName());
    }

    public File unpackMap(int id) {
        File f = this.getMapFolderFromID(id);
        if (f.exists()) return f;
        else return FileUtils.unzipMapFromMapPack(this.mapPack, this.mapsFolder, "map" + id);
    }

    public void movePlayerToWorld(EntityPlayerMP entity, int worldid) {
        if (!entity.isDead) {
            WorldServer current = this.playerWorldMap.get(entity);
            WorldServer next = this.worlds.get(worldid);
            System.out.println(current + " - " + next);
            entity.worldObj.removeEntity(entity);
            entity.isDead = false;
            Nailed.server.getConfigurationManager().transferEntityToWorld(entity, 1, current, next);
            Entity newEntity = EntityList.createEntityByName(EntityList.getEntityString(entity), next);
            if (newEntity != null) {
                newEntity.copyDataFrom(entity, true);
                ChunkCoordinates var7 = next.getSpawnPoint();
                var7.posY = entity.worldObj.getTopSolidOrLiquidBlock(var7.posX, var7.posZ);
                newEntity.setLocationAndAngles((double) var7.posX, (double) var7.posY, (double) var7.posZ, newEntity.rotationYaw, newEntity.rotationPitch);
                next.spawnEntityInWorld(newEntity);
            }
            entity.isDead = true;
            current.resetUpdateEntityTick();
            next.resetUpdateEntityTick();
        }
    }

    public void prepareSpawnForWorld(int id) {
        WorldServer world = this.worlds.get(id);
        ChunkCoordinates spawn = NailedEventFactory.getSpawnPoint();
        int var5 = 0;
        long var9 = System.currentTimeMillis();
        for (int var11 = -192; var11 <= 192; var11 += 16) {
            for (int var12 = -192; var12 <= 192; var12 += 16) {
                long var13 = System.currentTimeMillis();
                if (var13 - var9 > 1000L) {
                    System.out.println("Preparing spawn area for map " + id + ": " + (var5 * 100 / 625) + "%");
                    var9 = var13;
                }

                ++var5;
                world.theChunkProviderServer.loadChunk(spawn.posX + var11 >> 4, spawn.posZ + var12 >> 4);
            }
        }
    }
}
