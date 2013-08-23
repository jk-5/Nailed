package jk_5.nailed.command;

import jk_5.nailed.util.ServerUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import java.util.List;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class CommandTest extends CommandBase {

    public String getCommandName() {
        return "newmap";
    }

    public int getRequiredPermissionLevel() {
        return 3;
    }

    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "Creates a new map";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
        ServerUtils.broadcastChatMessage("HACKS");
    }
}
