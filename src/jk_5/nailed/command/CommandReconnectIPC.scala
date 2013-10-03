package jk_5.nailed.command

import jk_5.nailed.ipc.IPCClient

/**
 * No description given
 *
 * @author jk-5
 */
object CommandReconnectIPC extends TCommand {
  val commandName = "reconnectipc"
  this.permissionLevel = 2
  @inline override def getCommandUsage = "/reconnectipc - Reconnects to the ipc server"
  @inline def processCommand(sender: CommandSender, args: Array[String]) = IPCClient.reconnect()
}
