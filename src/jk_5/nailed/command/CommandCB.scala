package jk_5.nailed.command

import net.minecraft.src.{CommandException, TileEntityCommandBlock, ICommandSender, CommandBase}
import jk_5.nailed.Nailed

/**
 * No description given
 *
 * @author jk-5
 */
object CommandCB extends CommandBase {

  @inline def getCommandName = "cb"
  @inline override def getRequiredPermissionLevel = 2
  @inline def getCommandUsage(sender: ICommandSender) = "/cb - Command for command block interaction with Nailed";
  @inline def processCommand(sender: ICommandSender, args: Array[String]) = sender match {
    case cb: TileEntityCommandBlock => {
      val currentMap = Nailed.mapLoader.getMap(cb)
      args(0) match{
        case "startgame" => currentMap.getGameThread.start()
        case "setwinner" => currentMap.getGameThread.setWinner(currentMap.getTeamManager.getTeam(args(1)))
      }
    }
    case e => throw new CommandException("This command can only be used by command blocks")
  }
}
