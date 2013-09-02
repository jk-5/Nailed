package jk_5.nailed.command;

import jk_5.nailed.Nailed;
import net.minecraft.src.CommandBase;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommandSender;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class CommandNewWorld extends CommandBase {

    @Override
    public String getCommandName() {
        return "newworld";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "/newworld - Teleports you to a new world";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            Nailed.multiworldManager.movePlayerToWorld(player, 1);
        }
    }
}
