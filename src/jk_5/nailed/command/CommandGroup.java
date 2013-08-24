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
public class CommandGroup extends CommandBase {

    @Override
    public String getCommandName() {
        return "group";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "/group - Sets the group of a player";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length == 0){
            throw new WrongUsageException("/group <set>");
        }else if(args.length == 1){
            if(args[0].equals("set")){
                throw new WrongUsageException("/group set <player> <admin:player>");
            }
        }else if(args.length == 2){
            if(args[0].equals("set")){
                Player p = Nailed.playerRegistry.getPlayer(args[1]);
                if(p == null) throw new CommandException("Player " + args[1] + " was not found");
            }
        }else if(args.length == 3){
            if(args[0].equals("set")){
                Player p = Nailed.playerRegistry.getPlayer(args[1]);
                Group g = Nailed.groupRegistry.getGroup(args[2]);
                if(p == null) throw new CommandException("Player " + args[1] + " was not found");
                if(g == null) throw new CommandException("Group " + args[2] + " was not found");
                p.setGroup(g);
                p.sendChatMessage(EnumColor.GREEN + "You were moved to the group " + g.getName());
                sender.sendChatToPlayer(ChatMessageComponent.func_111066_d(EnumColor.GREEN + "Moved player " + p.getUsername() + " to group " + g.getName()));
            }
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 1){
            return getListOfStringsMatchingLastWord(args, new String[]{"set"});
        }else if(args.length == 2){
            if(args[0].equals("set")){
                return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
            }
        }else if(args.length == 3){
            if(args[0].equals("set")){
                return getListOfStringsFromIterableMatchingLastWord(args, Nailed.groupRegistry.getGroups());
            }
        }
        return null;
    }
}
