package jk_5.nailed.command

/**
 * No description given
 *
 * @author jk-5
 */
object CommandReady extends TCommand {
  val commandName = "ready"
  override def canCommandSenderUseCommand(sender: CommandSender) = sender.isValidPlayer && sender.player.isTeamLeader
  override def getCommandUsage = "/ready - Tells the game that your team is ready"
  def processCommand(sender: CommandSender, args: Array[String]){
    if(sender.isValidPlayer) sender.player.getTeam.setReady(!sender.player.getTeam.isReady)
  }
}
