package jk_5.nailed.map

import java.util.concurrent.atomic.AtomicInteger
import jk_5.nailed.map.gameloop.GameThread
import jk_5.nailed.players.{Player, PlayerRegistry}
import jk_5.nailed.util.EnumColor
import jk_5.nailed.Nailed
import java.io.File
import net.minecraft.world.WorldServer

/**
 * No description given
 *
 * @author jk-5
 */
object Map {
  private final val nextId = new AtomicInteger(0)
}

class Map(private final val mappack: Mappack) {

  private final val UID = Map.nextId.getAndIncrement
  private final val gameThread = new GameThread(this)
  private final val teamManager = new TeamManager(this)
  private var world: WorldServer = _

  def travelPlayerToMap(player: Player){
    val entity = player.getEntity
    if(entity.isEmpty) return
    Nailed.server.getConfigurationManager.respawnPlayer(entity.get, 0, true, this)
  }

  def setWorldServer(server: WorldServer){
    this.mappack.configureServer(server)
    this.world = server
    this.teamManager.setupTeams()
    println("Constructed map " + this.getUID + " with server " + this.world)
  }

  @inline def sendMessageToAllPlayersWithoutPrefix(msg: String) = this.getPlayers.foreach(_.sendChatMessage(msg))
  @inline def sendMessageToAllPlayers(msg: String) = this.sendMessageToAllPlayersWithoutPrefix(EnumColor.GREEN + "[" + this.getMappack.mapName + "] " + EnumColor.RESET + msg)

  @inline def getScoreboard = this.getWorld.getScoreboard
  @inline def getWorld = this.world
  @inline def getUID = this.UID
  @inline def getMappack = this.mappack
  @inline def getTeamManager = this.teamManager
  @inline def getFolderName = "map%d%s".format(this.getUID, this.getMappack.internalName)
  @inline def getGameThread = this.gameThread
  @inline def getPlayers = PlayerRegistry.getPlayers.filter(_.getCurrentMap == this)
  @inline def getWorldFolder = new File(MapLoader.mapsFolder, this.getFolderName)
}
