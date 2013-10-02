package jk_5.nailed.map

import java.util.concurrent.atomic.AtomicInteger
import java.io.{IOException, InputStreamReader, FileInputStream, File}
import scala.collection.immutable
import jk_5.nailed.map.gameloop.IInstruction
import net.minecraft.src.EnumGameType
import java.util.zip.{ZipEntry, ZipInputStream}
import jk_5.nailed.config.helper.ConfigFile
import org.apache.commons.io.IOUtils

/**
 * No description given
 *
 * @author jk-5
 */
object Mappack {
  private final val nextId = new AtomicInteger(0)

  def readMapConfig(mappack: Mappack){
    var stream: ZipInputStream = null
    var foundConfig = false
    var foundGameInstructions = false
    try {
      stream = new ZipInputStream(new FileInputStream(mappack.mappackFile))
      var entry: ZipEntry = null
      while ((entry = stream.getNextEntry) != null) {
        if (entry.getName.equals("mappack.cfg")) {
          foundConfig = true
          mappack.config = new ConfigFile(null, new InputStreamReader(stream)).setReadOnly(true)
        } else if (entry.getName().equals("gameinstructions.cfg")) {
          foundGameInstructions = true
          mappack.readInstructions(stream)
        }
      }
    }catch{
      case e: IOException => throw new MappackInitializationException(mappack, e)
    }finally IOUtils.closeQuietly(stream)
    if (!foundConfig) throw new MappackInitializationException(mappack, "Could not find a mappack.cfg file")
    if (!foundGameInstructions) throw new MappackInitializationException(mappack, "Could not find a gameinstructions.cfg file")
  }
}
class Mappack(private final val mappackFile: File) {
  private final val UID = Mappack.nextId.getAndIncrement
  private final val internalName = this.mappackFile.getName.split(".mappack", 2)(0)
  private final val instructions = immutable.List[IInstruction]()

  private var config: ConfigFile = _

  private var defaultGamemode: EnumGameType = _
  private var difficulty: Int = _
  private var mapName = this.internalName
  private var spawnHostileMobs: Boolean = _
  private var spawnFriendlyMobs: Boolean = _
  private var pvp: Boolean = _

  private var spawnX: Int = _
  private var spawnY: Int = _
  private var spawnZ: Int = _

  Mappack.readMapConfig(this)

  public void setupSettings() {
    this.defaultGamemode = EnumGameType.getByID(config.getTag("map").getTag("gamemode").getIntValue(EnumGameType.SURVIVAL.getID()));
    this.difficulty = config.getTag("map").getTag("difficulty").getIntValue(2);
    this.spawnHostileMobs = config.getTag("map").getTag("spawn-hostile-mobs").getBooleanValue(true);
    this.spawnFriendlyMobs = config.getTag("map").getTag("spawn-frienly-mobs").getBooleanValue(true);
    this.spawnX = config.getTag("spawnpoint").getTag("x").getIntValue(0);
    this.spawnY = config.getTag("spawnpoint").getTag("y").getIntValue(0);
    this.spawnZ = config.getTag("spawnpoint").getTag("z").getIntValue(0);
    this.mapName = config.getTag("name").getValue(this.internalName);
    this.enablePvp = config.getTag("map").getTag("pvp").getBooleanValue(true);
    if (this.defaultGamemode == EnumGameType.NOT_SET) this.defaultGamemode = EnumGameType.SURVIVAL;
  }

  private void readInstructions(ZipInputStream stream) throws MappackInitializationException {
    BufferedReader in = new BufferedReader(new InputStreamReader(stream));
    this.instructions.clear();
    int lineNumber = 1;
    try {
      while (in.ready()) {
        String line = in.readLine();
        if (line == null) continue;
        if (line.startsWith("#") || line.isEmpty()) continue;
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

  public String getInternalName() {
    return this.internalName;
  }

  public String getMapName() {
    return this.mapName;
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

  public boolean isPvpEnabled(){
    return this.enablePvp;
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
