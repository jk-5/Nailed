package jk_5.nailed.command;

import jk_5.nailed.Nailed;
import jk_5.nailed.NailedEventFactory;
import jk_5.nailed.map.Map;
import jk_5.nailed.players.Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public class CommandServerMode extends CommandBase {

    @Override
    public String getCommandName() {
        return "mode";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        Player p = Nailed.playerRegistry.getPlayer(sender.getCommandSenderName());
        return (p != null && p.isAdmin()) || sender instanceof MinecraftServer;
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "/mode <play:build:dev> - Changes the server mode";
    }

    @Override
    public void processCommand(ICommandSender send, String[] args) {
        Player p = Nailed.playerRegistry.getPlayer(send.getCommandSenderName());
        if(args.length != 1) throw new WrongUsageException("/mode <play:build:dev>");
        if((p != null && p.isAdmin()) || send instanceof MinecraftServer){
            String mode = args[0];
            if(mode.equalsIgnoreCase("play") || mode.equalsIgnoreCase("build") || mode.equalsIgnoreCase("dev")){
                NailedEventFactory.mode = args[0];
            }else throw new WrongUsageException("/mode <play:build:dev>");
        }else throw new CommandException("You are not allowed to do this!");
    }

    @Override
    public List addTabCompletionOptions(ICommandSender send, String[] args) {
        if(!this.canCommandSenderUseCommand(send)) return null;
        if(args.length == 0) return getListOfStringsMatchingLastWord(args, "build","dev","play");
        return null;
    }
}
