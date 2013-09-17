package jk_5.nailed.command;

import jk_5.nailed.Nailed;
import jk_5.nailed.players.Player;
import net.minecraft.src.*;

/**
 * No description given
 *
 * @author jk-5
 */
public class CommandReady extends CommandBase {

    @Override
    public String getCommandName() {
        return "ready";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        Player p = Nailed.playerRegistry.getPlayer(sender.getCommandSenderName());
        return p != null && p.isTeamLeader();
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "/ready - Tells the game that your team is ready";
    }

    @Override
    public void processCommand(ICommandSender send, String[] args) {
        Player sender = Nailed.playerRegistry.getPlayer(send.getCommandSenderName());
        sender.getTeam().setReady(!sender.getTeam().isReady());
    }
}
