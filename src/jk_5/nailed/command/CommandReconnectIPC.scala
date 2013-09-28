package jk_5.nailed.command

import net.minecraft.src.{ICommandSender, CommandBase}
import jk_5.nailed.Nailed

/**
 * No description given
 *
 * @author jk-5
 */
object CommandReconnectIPC extends CommandBase {

  def getCommandName = "reconnectipc"
  def getCommandUsage(sender: ICommandSender) = "/reconnectipc - Reconnects to the ipc server"
  override def getRequiredPermissionLevel = 2
  def processCommand(sender: ICommandSender, args: Array[String]) = Nailed.ipc.reconnect()
}
