package jk_5.nailed.command

import jk_5.nailed.map.MapLoader

/**
 * No description given
 *
 * @author jk-5
 */
object CommandWorld extends TCommand {
  val commandName = "goto"
  this.permissionLevel = 2

  @inline override def getCommandUsage = "/goto"
  def processCommand(sender: CommandSender, args: Array[String]) = if(sender.isValidPlayer) MapLoader.getMap(args(0).toInt).foreach(_.travelPlayerToMap(sender.player))
}
