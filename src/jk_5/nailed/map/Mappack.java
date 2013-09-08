package jk_5.nailed.map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jk_5.nailed.Nailed;
import jk_5.nailed.config.helper.ConfigFile;
import jk_5.nailed.map.gameloop.IInstruction;
import jk_5.nailed.map.gameloop.instructions.*;
import jk_5.nailed.util.FileUtils;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.EnumGameType;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This object is made for each mappack
 *
 * @author jk-5
 */
public class Mappack {

    private static final java.util.Map<String, Class<?>> instructionMap = Maps.newHashMap();
    private static final AtomicInteger nextId = new AtomicInteger(0);

    static {
        instructionMap.put("trigger", InstructionTrigger.class);
        instructionMap.put("sleep", InstructionSleep.class);
        instructionMap.put("watchunready", InstructionWatchUnready.class);
        instructionMap.put("unwatchunready", InstructionUnwatchUnready.class);
        instructionMap.put("countdown", InstructionCountdown.class);
        instructionMap.put("setwinner", InstructionSetWinner.class);
        instructionMap.put("startwinnerinterrupt", InstructionStartWinnerInterrupt.class);
        instructionMap.put("enable", InstructionEnableStat.class);
        instructionMap.put("disable", InstructionDisableStat.class);
        instructionMap.put("setspawn", InstructionSetSpawnpoint.class);
        instructionMap.put("resetspawn", InstructionResetSpawnpoint.class);
        instructionMap.put("clearinventory", InstructionClearInventory.class);
        instructionMap.put("setgamemode", InstructionSetGamemode.class);
        instructionMap.put("moveteamspeak", InstructionMoveTeamspeak.class);
    }

    private final List<IInstruction> instructions = Lists.newArrayList();

    private final int UID = nextId.getAndIncrement();
    private final String name;
    private final File mappackFile;

    private ConfigFile config;

    private EnumGameType defaultGamemode;
    private int difficulty;
    private boolean spawnHostileMobs;
    private boolean spawnFriendlyMobs;

    private int spawnX;
    private int spawnY;
    private int spawnZ;

    public Mappack(File mapfile) {
        this.mappackFile = mapfile;
        this.name = mapfile.getName().split(".mappack", 2)[0];
    }

    public void setupSettings() {
        this.defaultGamemode = EnumGameType.getByID(config.getTag("map").getTag("gamemode").getIntValue(EnumGameType.SURVIVAL.getID()));
        this.difficulty = config.getTag("map").getTag("difficulty").getIntValue(2);
        this.spawnHostileMobs = config.getTag("map").getTag("spawn-hostile-mobs").getBooleanValue(true);
        this.spawnFriendlyMobs = config.getTag("map").getTag("spawn-frienly-mobs").getBooleanValue(true);
        this.spawnX = config.getTag("spawnpoint").getTag("x").getIntValue(0);
        this.spawnY = config.getTag("spawnpoint").getTag("y").getIntValue(0);
        this.spawnZ = config.getTag("spawnpoint").getTag("z").getIntValue(0);
        if (this.defaultGamemode == EnumGameType.NOT_SET) this.defaultGamemode = EnumGameType.SURVIVAL;

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
                    this.readInstructions(stream);
                }
            }
        } catch (IOException e) {
            throw new MappackInitializationException(this, e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
        if (!foundConfig) throw new MappackInitializationException(this, "Could not find a mappack.cfg file");
        if (!foundGameInstructions)
            throw new MappackInitializationException(this, "Could not find a gameinstructions.cfg file");
    }

    private void readInstructions(ZipInputStream stream) throws MappackInitializationException {
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        this.instructions.clear();
        int lineNumber = 1;
        try {
            while (in.ready()) {
                String line = in.readLine();
                if (line == null) continue;
                if (line.startsWith("#")) continue;
                String data[] = line.split(" ", 2);
                if (data.length == 0) continue;
                IInstruction instr = getInstruction(data[0].trim());
                if (instr == null) continue;
                if (data.length == 2) instr.injectArguments(data[1]);
                this.instructions.add(instr);
                lineNumber++;
            }
        } catch (Exception e) {
            throw new MappackInitializationException(this, "Error while parsing instructions file at line " + lineNumber, e);
        }
    }

    private static IInstruction getInstruction(String name) {
        try {
            return (IInstruction) instructionMap.get(name).newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public List<IInstruction> getInstructions() {
        return this.instructions;
    }

    public void unpackMappack(File destinationDir) {
        FileUtils.unzipMapFromMapPack(this.mappackFile, destinationDir);
    }

    public int getUID() {
        return this.UID;
    }

    public String getName() {
        return this.name;
    }

    public ConfigFile getConfig() {
        return this.config;
    }

    public EnumGameType getDefaultGamemode() {
        return this.defaultGamemode;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public boolean shouldSpawnHostileMobs() {
        return this.spawnHostileMobs;
    }

    public boolean shouldSpawnFriendlyMobs() {
        return this.spawnFriendlyMobs;
    }

    public ChunkCoordinates getSpawnPoint() {
        return new ChunkCoordinates(this.spawnX, this.spawnY, this.spawnZ);
    }
}
