package jk_5.nailed.map;

import jk_5.nailed.Nailed;
import jk_5.nailed.config.helper.ConfigFile;
import jk_5.nailed.map.gameloop.GameThread;
import jk_5.nailed.util.FileUtils;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.WorldServer;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This object is made for each mappack
 *
 * @author jk-5
 */
public class Mappack {

    private static final AtomicInteger nextId = new AtomicInteger(0);

    private final int UID = nextId.getAndIncrement();
    private final String name;
    private final File mappackFile;

    private ConfigFile config;
    private GameThread gameThread; //TODO: move to Map

    private EnumGameType defaultGamemode;
    private int difficulty;
    private boolean spawnHostileMobs;
    private boolean spawnFriendlyMobs;

    private int spawnX;
    private int spawnY;
    private int spawnZ;

    public Mappack(File mapfile){
        this.mappackFile = mapfile;
        this.name = mapfile.getName().split(".mappack", 2)[0];
        this.gameThread = new GameThread(this);
    }

    public void setupSettings(){
        this.defaultGamemode = EnumGameType.getByID(config.getTag("map").getTag("gamemode").getIntValue(EnumGameType.SURVIVAL.getID()));
        this.difficulty = config.getTag("map").getTag("difficulty").getIntValue(2);
        this.spawnHostileMobs = config.getTag("map").getTag("spawn-hostile-mobs").getBooleanValue(true);
        this.spawnFriendlyMobs = config.getTag("map").getTag("spawn-frienly-mobs").getBooleanValue(true);
        this.spawnX = config.getTag("spawnpoint").getTag("x").getIntValue(0);
        this.spawnY = config.getTag("spawnpoint").getTag("y").getIntValue(0);
        this.spawnZ = config.getTag("spawnpoint").getTag("z").getIntValue(0);
        if(this.defaultGamemode == EnumGameType.NOT_SET) this.defaultGamemode = EnumGameType.SURVIVAL;

        Nailed.server.setAllowPvp(config.getTag("map").getTag("pvp").getBooleanValue(true));
        Nailed.server.setTexturePack(config.getTag("texturepackUrl").getValue(""));
        Nailed.server.setAllowFlight(true);
        Nailed.server.func_104055_i(true);  //setForceGamemode
    }

    public void readConfig() throws MappackInitializationException {
        ZipInputStream stream = null;
        boolean foundConfig = false;
        boolean foundGameInstructions = false;
        try {
            stream = new ZipInputStream(new FileInputStream(this.mappackFile));
            ZipEntry entry;
            while ((entry = stream.getNextEntry()) != null) {
                if (entry.getName().equals("mappack.cfg")) {
                    foundConfig = true;
                    this.config = new ConfigFile(null, new InputStreamReader(stream)).setReadOnly(true);
                } else if (entry.getName().equals("gameinstructions.cfg")) {
                    foundGameInstructions = true;
                    this.gameThread.readInstructions(stream);
                }
            }
        } catch (IOException e) {
            throw new MappackInitializationException(this, e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
        if (!foundConfig) throw new MappackInitializationException(this, "Could not find a mappack.cfg file");
        if (!foundGameInstructions) throw new MappackInitializationException(this, "Could not find a gameinstructions.cfg file");
    }

    public void unpackMappack(File destinationDir){
        FileUtils.unzipMapFromMapPack(this.mappackFile, destinationDir);
    }

    public int getUID(){
        return this.UID;
    }

    public String getName(){
        return this.name;
    }

    public GameThread getGameThread(){
        return this.gameThread;
    }

    public ConfigFile getConfig(){
        return this.config;
    }

    public EnumGameType getDefaultGamemode(){
        return this.defaultGamemode;
    }

    public int getDifficulty(){
        return this.difficulty;
    }

    public boolean shouldSpawnHostileMobs(){
        return this.spawnHostileMobs;
    }

    public boolean shouldSpawnFriendlyMobs(){
        return this.spawnFriendlyMobs;
    }

    public ChunkCoordinates getSpawnPoint(){
        return new ChunkCoordinates(this.spawnX, this.spawnY, this.spawnZ);
    }
}
