package jk_5.nailed.command;

import jk_5.nailed.Nailed;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandBase;
import net.minecraft.src.CommandException;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.TileEntityCommandBlock;

import java.util.List;

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
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "/cb - Command for command block interaction with Nailed";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof TileEntityCommandBlock) {

        } else throw new CommandException("This command can only be used by command blocks");
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, new String[]{"set"});
        } else if (args.length == 2) {
            if (args[0].equals("set")) {
                return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
            }
        } else if (args.length == 3) {
            if (args[0].equals("set")) {
                return getListOfStringsFromIterableMatchingLastWord(args, Nailed.groupRegistry.getGroups());
            }
        }
        return null;
    }
}
