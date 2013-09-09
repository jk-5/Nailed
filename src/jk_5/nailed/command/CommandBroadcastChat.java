package jk_5.nailed.command;

import com.google.common.base.Joiner;
import jk_5.nailed.Nailed;
import jk_5.nailed.players.Player;
import jk_5.nailed.teams.Team;
import jk_5.nailed.util.ServerUtils;
import net.minecraft.src.CommandBase;
import net.minecraft.src.CommandException;
import net.minecraft.src.ICommandSender;

/**
 * No description given
 *
 * @author jk-5
 */
public class CommandBroadcastChat extends CommandBase {

    @Override
    public String getCommandName() {
        return "c";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        Player p = Nailed.playerRegistry.getPlayer(sender.getCommandSenderName());
        return p != null && p.getTeam() != Team.UNKNOWN;
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "/c [message] - Broadcasts an chat message";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        Player p = Nailed.playerRegistry.getPlayer(sender.getCommandSenderName());
        if (p == null) throw new CommandException("I don\'t know who you are!");
        ServerUtils.broadcastChatMessage(p.formatChatMessage(Joiner.on(" ").join(args)));
    }
}
