package jk_5.nailed.command;

import jk_5.nailed.Nailed;
import jk_5.nailed.groups.Group;
import jk_5.nailed.players.Player;
import jk_5.nailed.util.EnumColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import java.util.List;

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
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "/newworld - Teleports you to a new world";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP) sender;
            player.mcServer.getConfigurationManager().transferPlayerToDimension(player, 3);
        }
    }
}
