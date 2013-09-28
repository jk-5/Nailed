package jk_5.nailed.command

import net.minecraft.src.{ICommandSender, CommandBase}
import jk_5.nailed.players.PlayerRegistry
import jk_5.nailed.Nailed

/**
 * No description given
 *
 * @author jk-5
 */
object CommandWorld extends CommandBase {

  @inline def getCommandName = "goto"
  @inline override def getRequiredPermissionLevel = 2
  @inline def getCommandUsage(sender: ICommandSender) = "/goto"
  def processCommand(sender: ICommandSender, args: Array[String]){
    val p = PlayerRegistry.getPlayer(sender.getCommandSenderName)
    if(p.isDefined) Nailed.mapLoader.getMap(args(0).toInt).travelPlayerToMap(p)
  }
}
