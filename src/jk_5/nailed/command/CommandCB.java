package jk_5.nailed.command;

import jk_5.nailed.Nailed;
import jk_5.nailed.map.Map;
import jk_5.nailed.teams.Team;
import net.minecraft.src.*;

/**
 * No description given
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
