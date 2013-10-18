package jk_5.nailed.team

import jk_5.nailed.util.EnumColor
import jk_5.nailed.players.{PlayerRegistry, Player}
import jk_5.nailed.map.Map
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraft.util.ChunkCoordinates

/**
 * No description given
 *
 * @author jk-5
 */
object Team{
  final val UNKNOWN = new Team("unknown")
}
case class Team(private final val teamId: String) {
  private var name: String = this.teamId
  private var color: EnumColor = _
  private var leader: Player = _
  private var ready = false
  private var friendlyFire = false
  private var scoreboardTeam: ScorePlayerTeam = _
  private var map: Map = _
  private var spawn: ChunkCoordinates = _

  @inline def sendChatMessage(message: String) = PlayerRegistry.getPlayers.filter(_.getTeam == this).foreach(_.sendChatMessage(message))
  @inline override def toString = this.color.toString + this.name + EnumColor.RESET
  @inline def shouldOverrideSpawnpoint = if(this.map == null) false else if(this.spawn == null) false else true
  @inline def toChatFormatString = this match{
    case Team.UNKNOWN => ""
    case t => "%s* %s%s ".format(this.getColor.toString, this.getName, EnumColor.RESET)
  }

  def setReady(ready: Boolean){
    this.ready = ready
    if(this.isReady) this.map.sendMessageToAllPlayers("Team " + this.toString + " is ready!")
    else this.map.sendMessageToAllPlayers("Team " + this.toString + " is ready!")
    this.map.getGameThread.notifyReadyUpdate()
  }

  @inline def getColor = this.color
  @inline def getName = this.name
  @inline def getTeamID = this.teamId
  @inline def getSpawnpoint = if(this.shouldOverrideSpawnpoint) this.spawn else null
  @inline def getTeamLeader = this.leader
  @inline def isReady = this.ready
  @inline def friendlyFireEnabled = this.friendlyFire
  @inline def getScoreboardTeam: Option[ScorePlayerTeam] = Option(this.scoreboardTeam)
  @inline def getAllPlayerNames = PlayerRegistry.getPlayers.filter(_.getTeam == this).map(_.getUsername)

  @inline def setMap(map: Map) = this.map = map
  @inline def setSpawnpoint(coords: ChunkCoordinates) = this.spawn = coords
  @inline def setTeamLeader(player: Player) = this.leader = player
  @inline def setFriendlyFireEnabled(on: Boolean) = this.friendlyFire = on
  @inline def setName(name: String) = this.name = name
  @inline def setColor(color: EnumColor) = this.color = color
  @inline def setScoreboardTeam(st: ScorePlayerTeam) = this.scoreboardTeam = st
}
