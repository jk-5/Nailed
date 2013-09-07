package jk_5.nailed.command;

import jk_5.nailed.Nailed;
import jk_5.nailed.players.Player;
import net.minecraft.src.CommandBase;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommandSender;

/**
 * No description given
 *
 * @author jk-5
 */
public class CommandNewWorld extends CommandBase {

    @Override
    public String getCommandName() {
        return "goto";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "/goto";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        Player p = Nailed.playerRegistry.getPlayer(sender.getCommandSenderName());
        if(p == null) return;
        Nailed.mapLoader.getMap(Integer.parseInt(args[0])).travelPlayerToMap(p);
    }
}
