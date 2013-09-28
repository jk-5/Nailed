package jk_5.nailed.command

import net.minecraft.src.{CommandException, ICommandSender, CommandBase}
import jk_5.nailed.Nailed
import jk_5.nailed.teams.Team
import jk_5.nailed.util.ServerUtils
import jk_5.nailed.players.PlayerRegistry

/**
 * No description given
 *
 * @author jk-5
 */
object CommandBroadcastChat extends CommandBase {
  override def getCommandName = "c"
  override def getRequiredPermissionLevel = 0
  override def canCommandSenderUseCommand(sender: ICommandSender): Boolean = {
    val player = PlayerRegistry.getPlayer(sender.getCommandSenderName)
    player.isDefined && player.get.getTeam == Team.UNKNOWN
  }
  def getCommandUsage(sender: ICommandSender) = "/c [message] - Broadcasts an chat message"
  def processCommand(sender: ICommandSender, args: Array[String]){
    val player = PlayerRegistry.getPlayer(sender.getCommandSenderName)
    if(player.isEmpty) throw new CommandException("I don\'t know who you are!")
    ServerUtils.broadcastChatMessage(player.get.formatChatMessage(args.mkString(" ")))
  }
}
