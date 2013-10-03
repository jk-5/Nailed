package jk_5.nailed

import com.google.common.eventbus.EventBus
import jk_5.nailed.config.helper.ConfigFile
import java.io.File
import jk_5.nailed.map.MapLoader
import jk_5.nailed.irc.IrcConnector
import jk_5.nailed.teamspeak3.TeamspeakManager
import net.minecraft.src._
import jk_5.nailed.groups.{GroupRegistry, GroupAdmin, GroupPlayer}
import jk_5.nailed.ipc.IPCClient
import jk_5.nailed.command._
import scala.collection.JavaConversions._

/**
 * No description given
 *
 * @author jk-5
 */
object Nailed {
  final val eventBus = new EventBus
  final val config = new ConfigFile(new File("nailed.cfg")).setComment("Nailed main config file")
  final val irc = new IrcConnector

  var server: DedicatedServer = _

  def init(server: DedicatedServer){
    this.server = server

    this.eventBus.register(NailedEventListener)

    MapLoader.loadMaps()

    GroupRegistry.registerGroup("player", new GroupPlayer())
    GroupRegistry.registerGroup("admin", new GroupAdmin())
    GroupRegistry.setDefaultGroup("player")

    MapLoader.setupLobby()

    this.irc.connect()
    IPCClient.start()

    TeamspeakManager.connect()
  }

  def onWorldReady() {
    this.registerCommands()

    this.server.setAllowFlight(true)
    this.server.func_104055_i(false)  //setForceGamemode

    //val map1 = this.mapLoader.createWorld(mapLoader.getMappack("normalLobby"))
    //val map2 = this.mapLoader.createWorld(mapLoader.getMappack("raceforwool"))

    MapLoader.setLobbyWorld(this.server.worldServerForDimension(0))
  }

  def registerCommands(){
    val handler = this.server.getCommandManager.asInstanceOf[ServerCommandManager]
    handler.getCommands.foreach(command => command._2 match {
        case c: CommandDefaultGameMode => handler.getCommands.remove(c)
        case c: CommandShowSeed => handler.getCommands.remove(c)
        case c: ServerCommandScoreboard => handler.getCommands.remove(c)
        case c: CommandServerWhitelist => handler.getCommands.remove(c)
        case _ =>
      }
    )
    handler.registerCommand(CommandCB)
    handler.registerCommand(CommandGroup)
    handler.registerCommand(CommandTeam)
    handler.registerCommand(CommandWorld)
    handler.registerCommand(CommandSpectator)
    handler.registerCommand(CommandStartGame)
    handler.registerCommand(CommandBroadcastChat)
    handler.registerCommand(CommandTeamleader)
    handler.registerCommand(CommandReady)
    handler.registerCommand(CommandServerMode)
    handler.registerCommand(CommandReconnectIPC)
    if (TeamspeakManager.isEnabled) handler.registerCommand(CommandTeamspeak)
  }
}
