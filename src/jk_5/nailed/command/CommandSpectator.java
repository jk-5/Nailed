package jk_5.nailed.command;

import jk_5.nailed.Nailed;
import jk_5.nailed.players.Player;
import net.minecraft.src.CommandBase;
import net.minecraft.src.CommandException;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommandSender;

/**
 * No description given
 *
 * @author jk-5
 */
public class CommandSpectator extends CommandBase {

    @Override
    public String getCommandName() {
        return "spectator";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender instanceof EntityPlayerMP;
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "/spectator - Toggles spectator mode";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        Player p = Nailed.playerRegistry.getPlayer(sender.getCommandSenderName());
        if (p == null) throw new CommandException("I seriously don\'t know who you are!");
        p.setSpectator(!p.isSpectator());
    }
}
