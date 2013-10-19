package jk_5.nailed.map

import java.io._
import jk_5.nailed.Nailed
import scala.collection.mutable
import jk_5.nailed.config.helper.ConfigFile
import java.util.zip.{ZipEntry, ZipInputStream}
import org.apache.commons.io.IOUtils
import net.minecraft.world.{WorldType, MinecraftException, WorldSettings, WorldServer}
import net.minecraft.command.ICommandSender
import net.minecraft.world.chunk.storage.{AnvilSaveHandler, AnvilSaveConverter}
import net.minecraft.server.ConvertingProgressUpdate
import net.minecraft.scoreboard.Scoreboard

/**
 * No description given
 *
 * @author jk-5
 */
object MapLoader {
  final val mappackFolder = new File("mappacks")
  final val mapsFolder = new File("maps")

  private final val maps = mutable.ArrayBuffer[Map]()
  private final val mappacks = mutable.ArrayBuffer[Mappack]()
  final val worlds = mutable.ArrayBuffer[WorldServer]()
  private var lobby: Map = null

  if(!this.mappackFolder.exists()) this.mappackFolder.mkdirs()

  def loadMaps(){
    this.mappacks.clear()
    for (file <- mappackFolder.listFiles(new FilenameFilter {
      def accept(file: File, name: String) = name.endsWith(".mappack")
    })) {
      var pack: Mappack = null
      try{
        pack = new Mappack(file, this.getConfigForMappack(file))
        this.readInstructions(pack)
        this.mappacks += pack
      }catch{
        case e: Exception => new MappackInitializationException(pack, e).printStackTrace()
      }
    }
  }

  private def getConfigForMappack(file: File): ConfigFile = {
    var config: ConfigFile = null
    var stream: ZipInputStream = null
    try {
      stream = new ZipInputStream(new FileInputStream(file))
      var entry = stream.getNextEntry
      while (entry != null) {
        if (entry.getName.equals("mappack.cfg")) {
          config = new ConfigFile(null, new InputStreamReader(stream)).setReadOnly(readonly = true)
        }
        entry = stream.getNextEntry
      }
    }catch{
      case e: IOException => throw new MappackInitializationException("Could not find mappack.cfg", e)
    }finally IOUtils.closeQuietly(stream)
    if(config == null) throw new MappackInitializationException("Could not find mappack.cfg")
    config
  }

  private def readInstructions(mappack: Mappack){
    var stream: ZipInputStream = null
    try {
      stream = new ZipInputStream(new FileInputStream(mappack.getFile))
      var entry = stream.getNextEntry
      while (entry != null) {
        if (entry.getName.equals("gameinstructions.cfg")) {
          val reader = new BufferedReader(new InputStreamReader(stream))
          mappack.readInstructions(reader)
          reader.close()
          stream.close()
          return
        }
        entry = stream.getNextEntry
      }
    }catch{
      case e: IOException => throw new MappackInitializationException("Error while reading gameinstructions.cfg", e)
    }finally IOUtils.closeQuietly(stream)
    throw new MappackInitializationException("Could not find gameinstructions.cfg")
  }

  def getMap(uid: Int): Option[Map] = this.maps.find(_.getUID == uid)
  def getMap(sender: ICommandSender): Map = this.getMapFromWorld(sender.getEntityWorld.asInstanceOf[WorldServer])

  def createWorld(mappack: Mappack): Map = {
    val map = new Map(mappack)
    mappack.unpack(map.getWorldFolder)
    if (this.getMap(map.getUID).isDefined) return map
    val folder = map.getWorldFolder
    if (folder.exists && !folder.isDirectory){
      throw new IllegalArgumentException("File exists with the name '" + folder.getName + "' and isn't a folder")
    }
    val converter = new AnvilSaveConverter(mapsFolder)
    if (converter.isOldMapFormat("map" + map.getFolderName)) {
      println("Converting world '" + map.getFolderName + "'")
      converter.convertMapFormat(map.getFolderName, new ConvertingProgressUpdate(Nailed.server))
    }
    val worldServer = new WorldServer(Nailed.server, new AnvilSaveHandler(mapsFolder, map.getFolderName, true), map.getFolderName, map.getUID, new WorldSettings(1L, mappack.getDefaultGamemode, false, false, WorldType.DEFAULT), Nailed.server.theProfiler, Nailed.server.getLogAgent)
    worldServer.worldScoreboard = new Scoreboard
    this.worlds += worldServer

    println("Preparing start region for level '" + map.getFolderName + "'")
    /*if (true) {
      val short1: Short = 196
      var i: Long = System.currentTimeMillis
      {
        var j: Int = -short1
        while (j <= short1) {
          {
            {
              var k: Int = -short1
              while (k <= short1) {
                {
                  val l: Long = System.currentTimeMillis
                  if (l < i) {
                    i = l
                  }
                  if (l > i + 1000L) {
                    val i1: Int = (short1 * 2 + 1) * (short1 * 2 + 1)
                    val j1: Int = (j + short1) * (short1 * 2 + 1) + k + 1
                    System.out.println("Preparing spawn area for " + map.getFolderName + ", " + (j1 * 100 / i1) + "%")
                    i = l
                  }
                  val chunkcoordinates: ChunkCoordinates = mappack.getSpawnPoint
                  worldServer.getWorldChunkManager.getBiomeGenAt(chunkcoordinates.posX + j >> 4, chunkcoordinates.posZ + k >> 4)
                }
                k += 16
              }
            }
          }
          j += 16
        }
      }
    }*/

    this.maps += map
    map.setWorldServer(worldServer)
    map
  }

  def unloadWorld(map: Map, save: Boolean): Boolean = {
    if (map == null) return false
    val handle = map.getWorld
    if (!this.worlds.contains(handle)) return false
    if (handle.playerEntities.size > 0) return false
    if (save) {
      try {
        handle.saveAllChunks(true, null)
        handle.saveLevel()
      }catch{
        case ex: MinecraftException => ex.printStackTrace()
      }
    }
    maps -= map
    worlds -= handle
    true
  }

  def setupLobby() {
    val lobby = this.getMappack("lobby")
    if(lobby.isEmpty) throw new Exception("No mappack with name 'lobby.mappack' was found")
    this.lobby = new Map(lobby.get)
    this.maps += this.lobby
    lobby.get.unpack(this.lobby.getWorldFolder)
    Nailed.server.setFolderName("maps/" + this.lobby.getFolderName)
  }

  @inline def getMappack(name: String): Option[Mappack] = this.mappacks.find(_.internalName == name)
  @inline def setLobbyWorld(world: WorldServer) = this.lobby.setWorldServer(world)
  @inline def getMapFromWorld(world: WorldServer): Map = this.maps.find(_.getWorld eq world).get
  @inline def getMappacks = this.mappacks
  @inline def getLobby = this.lobby
}
