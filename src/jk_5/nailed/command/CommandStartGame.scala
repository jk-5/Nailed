package jk_5.nailed.command

/**
 * No description given
 *
 * @author jk-5
 */
object CommandStartGame extends TCommand {
  val commandName = "startgame"
  this.permissionLevel = 2
  override def getCommandUsage = "/startgame - Starts a game"
  def processCommand(sender: CommandSender, args: Array[String]) = sender.map.getGameThread.start()
}
