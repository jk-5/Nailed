package jk_5.nailed.command

import net.minecraft.src.{ICommandSender, CommandBase}
import jk_5.nailed.Nailed
import jk_5.nailed.ipc.IPCClient

/**
 * No description given
 *
 * @author jk-5
 */
object CommandReconnectIPC extends CommandBase {

  @inline def getCommandName = "reconnectipc"
  @inline def getCommandUsage(sender: ICommandSender) = "/reconnectipc - Reconnects to the ipc server"
  @inline override def getRequiredPermissionLevel = 2
  @inline def processCommand(sender: ICommandSender, args: Array[String]) = IPCClient.reconnect()
}
