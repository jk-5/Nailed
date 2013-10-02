package jk_5.nailed.command

import jk_5.nailed.Nailed

/**
 * No description given
 *
 * @author jk-5
 */
object CommandWorld extends TCommand {
  val commandName = "goto"
  override val permissionLevel = 2

  @inline override def getCommandUsage = "/goto"
  def processCommand(sender: CommandSender, args: Array[String]) = if(sender.isValidPlayer) Nailed.mapLoader.getMap(args(0).toInt).travelPlayerToMap(sender.player)
}
