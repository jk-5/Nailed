package jk_5.nailed

import jk_5.nailed.util.EnumColor
import jk_5.nailed.players.PlayerRegistry
import net.minecraft.src._
import jk_5.nailed.groups.GroupRegistry
import jk_5.nailed.event.{PlayerChatEvent, PlayerLeaveServerEvent, PlayerJoinServerEvent}
import java.net.InetAddress
import java.util.Random
import jk_5.nailed.map.Map

/**
 * No description given
 *
 * @author jk-5
 */
object NailedEventFactory {

  private final val motd = Array[String]("Well, that escalated quickly!", "Let\'s go!", "Oh well...", "Hello world!", "That\'s me!", "Oh god why!?", "Oh i hate the teams!", "FUCK THIS SHIT!", "I hate you!", "Kill them all!", "Blow it up!", "Fix yo laggz bro!", "Where\'s the enderpearl?", "It\'s opensource!", "Gimme starfall!", EnumColor.RANDOM + "FUNKY SHIT!", "Now 99% bug-free!")
  var mode = sys.props.get("nailed.mode").getOrElse("play")

  def playerLoggedIn(entity: EntityPlayerMP, world: WorldServer){
    val p = PlayerRegistry.getOrCreatePlayer(entity.getCommandSenderName)
    if(p.getUsername.equals("Clank26") || p.getUsername.equals("PostVillageCore")) p.setGroup(GroupRegistry.getGroup("admin").get)
    Nailed.eventBus.post(new PlayerJoinServerEvent(p))
  }

  def playerLoggedOut(entity: EntityPlayerMP){
    val p = PlayerRegistry.getOrCreatePlayer(entity.getCommandSenderName)
    Nailed.eventBus.post(new PlayerLeaveServerEvent(p))
  }

  def onPlayerChat(entity: EntityPlayerMP, msg: String){
    val p = PlayerRegistry.getOrCreatePlayer(entity.getCommandSenderName)
    Nailed.eventBus.post(new PlayerChatEvent(p, msg))
  }

  @inline def isOp(username: String) = PlayerRegistry.getPlayer(username).exists(_.isAdmin)

  def getSpawnPoint(map: Map): ChunkCoordinates = map.getMappack.getSpawn
  def getSpawnPoint(playerEntity: EntityPlayerMP): ChunkCoordinates = {
    val player = PlayerRegistry.getOrCreatePlayer(playerEntity.getCommandSenderName)
    var spawn = player.getCurrentMap.getMappack.getSpawn
    if(player != null && player.getTeam != null && player.getTeam.shouldOverrideSpawnpoint){
      spawn = player.getTeam.getSpawnpoint
    }
    spawn
  }

  def canPlayerPickup(entity: EntityPlayer): Boolean = {
    PlayerRegistry.getPlayer(entity.getCommandSenderName).map(p => !p.isSpectator)
    false
  }

  def canPlayerAttackPlayer(a: EntityPlayer, b: EntityPlayer): Boolean = {
    val oA = PlayerRegistry.getPlayer(a.getCommandSenderName)
    val oB = PlayerRegistry.getPlayer(b.getCommandSenderName)
    if(oA.isEmpty || oB.isEmpty) return false
    val attacker = oA.get
    val attacked = oB.get
    if(attacker.isSpectator) return false
    if(!attacked.getCurrentMap.getMappack.isPvpEnabled) return false
    if(!attacker.getCurrentMap.getMappack.isPvpEnabled) return false
    if(attacker.getTeam == attacked.getTeam){
      return attacker.getTeam.friendlyFireEnabled
    }
    true
  }

  @inline def flushWorlds() = {}
  @inline def saveWorlds() = {}
  @inline def isServerPublic = this.mode.equals("play")
  @inline def isServerPublicForAddress(address: InetAddress): Boolean = this.isServerPublic || address.getHostName.equalsIgnoreCase("localhost")
  @inline def getProtocolVersion(address: InetAddress) = if(isServerPublicForAddress(address)) 74 else 5
  @inline def getMCVersionForList(address: InetAddress) = if(isServerPublicForAddress(address)) "1.6.2" else "Offline"
  @inline def getMotdForList: String =
    if(mode.equals("dev")) EnumColor.GREEN + "[Nailed]" + EnumColor.RESET + " Development Mode"
    else if(mode.equals("build")) EnumColor.GREEN + "[Nailed]" + EnumColor.RESET + " Build Mode"
    else EnumColor.GREEN + "[Nailed]" + EnumColor.RESET + " " + this.motd(new Random().nextInt(motd.length))

}
