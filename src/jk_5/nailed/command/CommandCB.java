package jk_5.nailed.command;

import jk_5.nailed.Nailed;
import jk_5.nailed.teams.Team;
import net.minecraft.src.CommandBase;
import net.minecraft.src.ICommandSender;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class CommandCB extends CommandBase {

    @Override
    public String getCommandName() {
        return "cb";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "/cb - Command for command block interaction with Nailed";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        //if (sender instanceof TileEntityCommandBlock) {
        if (args[0].equalsIgnoreCase("startgame")) {
            Nailed.mapManager.getThread().start();
        } else if (args[0].equalsIgnoreCase("setwinner")) {
            Team winner = Nailed.teamRegistry.getTeam(args[1]);
            Nailed.mapManager.getThread().setWinner(winner);
        }
        //} else throw new CommandException("This command can only be used by command blocks");
    }
}
