package jk_5.nailed.command

import jk_5.nailed.team.Team

/**
 * No description given
 *
 * @author jk-5
 */
object CommandBroadcastChat extends TCommand {
  val commandName = "c"
  override val permissionLevel = 0
  override def canCommandSenderUseCommand(sender: CommandSender) = sender.isValidPlayer && sender.player.getTeam != Team.UNKNOWN
  override def getCommandUsage = "/c [message] - Broadcasts an chat message"
  override def processCommand(sender: CommandSender, args: Array[String]){
    if(sender.isInvalidPlayer) throw new CommandException("I don\'t know who you are!")
    sender.map.sendMessageToAllPlayersWithoutPrefix(sender.player.formatChatMessage(args.mkString(" ")))
  }
}
