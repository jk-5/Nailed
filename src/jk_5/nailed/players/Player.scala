package jk_5.nailed.players

import jk_5.nailed.teamspeak3.TeamspeakManager
import jk_5.nailed.map.Map
import jk_5.nailed.team.Team
import jk_5.nailed.Nailed
import net.minecraft.server.MinecraftServer
import jk_5.nailed.util.EnumColor
import jk_5.nailed.groups.{GroupRegistry, Group}
import net.minecraft.src._

/**
 * No description given
 *
 * @author jk-5
 */
case class Player(private final val username: String) {

  private var teamspeakName: String = null
  private var currentMap: Map = _
  private var team = Team.UNKNOWN
  private var group = GroupRegistry.getDefaultGroup
  private var spectator = false

  def onLogin(){
    if(!Nailed.config.getTag("teamspeak").getTag("enabled").getBooleanValue(default = false)) return
    if(this.teamspeakName != null) return
    this.teamspeakName = if(TeamspeakManager.getClientForUser(this.username).isDefined) this.username else null
  }

  def setTeamspeakName(name: String){
    if(TeamspeakManager.isEnabled) return
    val changed = this.teamspeakName != name
    this.teamspeakName = name
    if(!changed) return
    if(this.teamspeakName != null) this.sendChatMessage(EnumColor.AQUA + "You are now linked to your teamspeak account " + this.teamspeakName)
    else this.sendChatMessage(EnumColor.AQUA + "There was no teamspeak client found with the same username as you. Change your teamspeak username so it matches your ingame name or do /ts setname")
  }

  def setTeam(team: Team){
    if(this.team != null) this.currentMap.getWorld.getScoreboard.removePlayerFromTeams(this.username)
    if(team.getScoreboardTeam.isDefined) this.currentMap.getWorld.getScoreboard.addPlayerToTeam(this.username, team.getScoreboardTeam.get)
    this.team = team
  }

  @inline def formatChatMessage(msg: String) =
    "%s%s%s%s%s%s[%s%s] %s".format(this.team.toChatFormatString, if(this.isTeamLeader) EnumColor.PURPLE + "[Teamleader] " + EnumColor.GREY else "", if(this.spectator) EnumColor.AQUA + "[Spectator] " else "", EnumColor.GREY, this.getGroup.getChatPrefix, EnumColor.GREY, this.getChatFormattedName, EnumColor.GREY, msg)

  def setSpectator(spectator: Boolean){
    if(this.spectator == spectator) return
    if(this.team != Team.UNKNOWN && this.currentMap.getGameThread.isGameRunning){
      this.sendChatMessage(EnumColor.RED + "You can not join spectator mode while you are in a game!")
      return
    }
    val entity = this.getEntity.getOrElse(null)
    if(entity == null) return
    if(spectator){
      this.spectator = true
      this.getCurrentMap.getWorld.getScoreboard.addPlayerToTeam(this.username, this.currentMap.getTeamManager.getSpectatorTeam)
      entity.addPotionEffect(new PotionEffect(Potion.invisibility.getId, 1000000, 0, true))
      entity.capabilities.allowEdit = false
      entity.capabilities.disableDamage = true
      entity.capabilities.allowFlying = true
      entity.capabilities.isFlying = true
      entity.sendPlayerAbilities()
      this.sendChatMessage(EnumColor.GREEN + "You are in spectator mode. To disable, type " + EnumColor.YELLOW + "/spectator" + EnumColor.GREEN + " again")
    }else{
      this.spectator = false
      this.currentMap.getWorld.getScoreboard.removePlayerFromTeams(this.username)
      entity.removePotionEffect(Potion.invisibility.getId)
      entity.capabilities.allowEdit = true
      entity.capabilities.disableDamage = false
      entity.capabilities.allowFlying = false
      entity.capabilities.isFlying = false
      entity.theItemInWorldManager.getGameType.configurePlayerCapabilities(entity.capabilities)
      entity.sendPlayerAbilities()
      this.sendChatMessage(EnumColor.GREEN + "You are no longer in spectator mode")
    }
  }

  @inline def playSound(name: String, volume: Float, pitch: Float) = this.getEntity.foreach(e=> this.sendPacket(new Packet62LevelSound(name, e.posX, e.posY, e.posZ, volume, pitch)))

  @inline def sendChatMessage(msg: String) = this.getEntity.foreach(_.sendChatToPlayer(ChatMessageComponent.createFromText(msg)))
  @inline def sendPacket(packet: Packet) = this.getEntity.foreach(_.playerNetServerHandler.sendPacket(packet))

  @inline def getUsername = this.username
  @inline def getGroup = this.group
  @inline def getEntity: Option[EntityPlayerMP] = Option(MinecraftServer.getServer.getConfigurationManager.getPlayerEntity(this.username))
  @inline def getTeamspeakName = this.teamspeakName
  @inline def getTeam = this.team
  @inline def getChatFormattedName = this.group.getNameColor + this.username + EnumColor.RESET.toString
  @inline def getCurrentMap = this.currentMap
  @inline def isSpectator = this.spectator
  @inline def isAdmin = this.group.getGroupID.equalsIgnoreCase("admin")
  @inline def isTeamLeader = this.team != Team.UNKNOWN && this.team.getTeamLeader == this

  @inline def setGroup(group: Group) = this.group = group
  @inline def setCurrentMap(map: Map) = this.currentMap = map
}
