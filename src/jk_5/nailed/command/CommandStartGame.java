package jk_5.nailed.command;

import jk_5.nailed.Nailed;
import net.minecraft.src.CommandBase;
import net.minecraft.src.ICommandSender;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class CommandStartGame extends CommandBase {

    @Override
    public String getCommandName() {
        return "startgame";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "/startgame - Starts a game";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        Nailed.mapManager.getGameThread().start();
    }
}
