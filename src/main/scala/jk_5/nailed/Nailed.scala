package jk_5.nailed

import com.google.common.eventbus.EventBus
import jk_5.nailed.config.helper.ConfigFile
import java.io.{IOException, File}
import jk_5.nailed.map.MapLoader
import jk_5.nailed.irc.IrcConnector
import jk_5.nailed.teamspeak3.TeamspeakManager
import jk_5.nailed.groups.{GroupRegistry, GroupAdmin, GroupPlayer}
import jk_5.nailed.ipc.IPCClient
import jk_5.nailed.command._
import scala.collection.JavaConversions._
import net.minecraft.server.dedicated.DedicatedServer
import net.minecraft.command.{CommandServerWhitelist, CommandShowSeed, CommandDefaultGameMode, ServerCommandManager}
import net.minecraft.scoreboard.ServerCommandScoreboard
import jline.console.ConsoleReader
import jk_5.nailed.logging.NailedLogging
import jk_5.nailed.util.JLineAutoCompleter

/**
 * No description given
 *
 * @author jk-5
 */
object Nailed {
  final val eventBus = new EventBus
  final val config = new ConfigFile(new File("nailed.cfg")).setComment("Nailed main config file")
  final val irc = new IrcConnector
  var reader: ConsoleReader = _

  var server: DedicatedServer = _
  var useJLine = true

  def init(server: DedicatedServer){
    NailedLogging.init()

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
    this.server.setForceGamemode(false)

    //val map1 = this.mapLoader.createWorld(mapLoader.getMappack("normalLobby"))
    //val map2 = this.mapLoader.createWorld(mapLoader.getMappack("raceforwool"))

    MapLoader.setLobbyWorld(this.server.worldServerForDimension(0))
  }

  def initJLine(){
    try{
      this.reader = new ConsoleReader(System.in, NailedLogging.sysOut)
      this.reader.setExpandEvents(false) //Avoid parsing exceptions for uncommonly used event designators
      this.reader.addCompleter(JLineAutoCompleter)
    }catch{
      case e: Exception => try {
        //Try again with jline disabled for Windows users without C++ 2008 Redistributable
        System.setProperty("jline.terminal", "jline.UnsupportedTerminal")
        System.setProperty("user.language", "en")
        this.useJLine = false
        this.reader = new ConsoleReader(System.in, System.out)
        this.reader.setExpandEvents(false)
      }catch{
        case e: IOException => e.printStackTrace()
      }
    }
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
