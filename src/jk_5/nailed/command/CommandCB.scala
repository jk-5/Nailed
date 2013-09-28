package jk_5.nailed.command

import net.minecraft.src.{ICommandSender, CommandBase}

/**
 * No description given
 *
 * @author jk-5
 */
object CommandCB extends CommandBase {

  def getCommandName = "cb"
  def getRequiredPermissionLevel = 2
  def getCommandUsage(sender: ICommandSender) {
    return "/cb - Command for command block interaction with Nailed";
  }

  @Override
  public void processCommand(ICommandSender send, String[] args) {
    if (send instanceof TileEntityCommandBlock) {
      TileEntityCommandBlock sender = (TileEntityCommandBlock) send;
      Map currentMap = Nailed.mapLoader.getMapFromWorld((WorldServer) sender.getWorldObj());
      if (args[0].equalsIgnoreCase("startgame")) {
        Nailed.mapLoader.getMap(0).getGameThread().start();
      } else if (args[0].equalsIgnoreCase("setwinner")) {
        Team winner = currentMap.getTeamManager().getTeam(args[1]);
        Nailed.mapLoader.getMap(0).getGameThread().setWinner(winner);
      }
    } else throw new CommandException("This command can only be used by command blocks");
  }
}
