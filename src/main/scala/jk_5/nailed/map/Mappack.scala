package jk_5.nailed.map

import java.util.concurrent.atomic.AtomicInteger
import java.io._
import scala.collection.{immutable, mutable}
import jk_5.nailed.map.gameloop.IInstruction
import jk_5.nailed.config.helper.ConfigFile
import jk_5.nailed.util.FileUtils
import net.minecraft.util.ChunkCoordinates
import net.minecraft.world.{WorldServer, EnumGameType}

/**
 * No description given
 *
 * @author jk-5
 */
object Mappack {
  private final val nextId = new AtomicInteger(0)
}
class Mappack(private final val mappackFile: File, private final val config: ConfigFile) {
  private final val _UID = Mappack.nextId.getAndIncrement
  private final val _internalName = this.mappackFile.getName.split(".mappack", 2)(0)
  private var _instructions = immutable.List[IInstruction]()

  private final val defaultGamemode = EnumGameType.getByID(config.getTag("map").getTag("gamemode").getIntValue(EnumGameType.SURVIVAL.getID))
  private final val difficulty = config.getTag("map").getTag("difficulty").getIntValue(2)
  private final val _mapName = config.getTag("name").getValue(this._internalName)
  private final val spawnHostileMobs = config.getTag("map").getTag("spawn-hostile-mobs").getBooleanValue(default = true)
  private final val spawnFriendlyMobs = config.getTag("map").getTag("spawn-friendly-mobs").getBooleanValue(default = true)
  private final val pvp = config.getTag("map").getTag("pvp").getBooleanValue(default = true)

  private final val spawnX = config.getTag("spawnpoint").getTag("x").getIntValue(0)
  private final val spawnY = config.getTag("spawnpoint").getTag("y").getIntValue(0)
  private final val spawnZ = config.getTag("spawnpoint").getTag("z").getIntValue(0)

  def readInstructions(reader: BufferedReader){
    val list = mutable.ListBuffer[IInstruction]()
    var lineNumber = 1
    try{
      while(reader.ready()){
        val line = reader.readLine()
        if(line != null && !line.isEmpty && !line.startsWith("#")){
          val data = line.split(" ", 2)
          if(data.length > 0){
            val instr = InstructionManager.getInstruction(data(0).trim)
            if(instr != null && data.length == 2) instr.injectArguments(data(1))
            list += instr
            lineNumber += 1
          }
        }
      }
    }catch{
      case e: Exception => throw new MappackInitializationException(this, "Error while parsing instructions file at line " + lineNumber, e)
    }
    this._instructions = list.toList
  }

  def configureServer(server: WorldServer){
    server.setAllowedSpawnTypes(this.shouldSpawnHostileMobs, this.shouldSpawnFriendlyMobs)
    server.difficultySetting = this.getDifficulty
  }

  @inline def instructions = this._instructions
  @inline def unpack(destinationDir: File) = FileUtils.unzipMapFromMapPack(this.mappackFile, destinationDir)
  @inline def UID = this._UID
  @inline def internalName = this._internalName
  @inline def mapName = this._mapName
  @inline def getConfig = this.config
  @inline def getDefaultGamemode = this.defaultGamemode
  @inline def getDifficulty = this.difficulty
  @inline def isPvpEnabled = this.pvp
  @inline def shouldSpawnHostileMobs = this.spawnHostileMobs
  @inline def shouldSpawnFriendlyMobs = this.spawnFriendlyMobs
  @inline def getSpawn = new ChunkCoordinates(this.spawnX, this.spawnY, this.spawnZ)
  @inline def getFile = this.mappackFile
}
