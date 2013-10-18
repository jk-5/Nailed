package jk_5.nailed.players

import scala.collection.mutable
import jk_5.nailed.map.MapLoader
import net.minecraft.command.ICommandSender

/**
 * No description given
 *
 * @author jk-5
 */
object PlayerRegistry {

  private final val players = new mutable.HashMap[String, Player]()

  def getPlayer(sender: ICommandSender): Option[Player] = this.getPlayer(sender.getCommandSenderName)
  def getPlayer(username: String): Option[Player] = this.players.get(username)
  def getPlayers = this.players.values
  def getOrCreatePlayer(username: String): Player =
    if(this.getPlayer(username).isDefined) this.getPlayer(username).get
    else{
      val player = new Player(username)
      this.players.put(username, player)
      player.setCurrentMap(MapLoader.getLobby)
      player
    }
}
