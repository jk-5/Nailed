package jk_5.nailed.multiworld;

import com.google.common.collect.Maps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.WorldManager;
import net.minecraft.src.WorldServer;
import net.minecraft.src.WorldServerMulti;
import net.minecraft.src.WorldSettings;

import java.io.File;
import java.util.Map;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class MultiworldManager {

    private Map<Integer, WorldServer> worlds = Maps.newHashMap();

    /*public WorldServer createNewMapDimension(int id, File map){
        WorldServer overworld = MinecraftServer.getServer().worldServerForDimension(0);
        WorldSettings settings = new WorldSettings(overworld.getWorldInfo());
        WorldServer newWorld = new WorldServerMulti(MinecraftServer.getServer(), overworld.getSaveHandler(), map.getName(), id, settings, overworld, MinecraftServer.getServer().theProfiler, overworld.getWorldLogAgent());
        newWorld.addWorldAccess(new WorldManager(MinecraftServer.getServer(), newWorld));
        newWorld.getWorldInfo().setGameType(MinecraftServer.getServer().getGameType());
        MinecraftServer.getServer().setDifficultyForAllWorlds(MinecraftServer.getServer().getDifficulty());
        return newWorld;
    }*/
}
