package jk_5.nailed.command;

import jk_5.nailed.Nailed;
import jk_5.nailed.map.Map;
import jk_5.nailed.teams.Team;
import jk_5.nailed.players.Player;
import jk_5.nailed.util.EnumColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public class CommandTeamleader extends CommandBase {

    @Override
    public String getCommandName() {
        return "teamleader";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "/teamleader - Sets the teamleaders";
    }

    @Override
    public void processCommand(ICommandSender send, String[] args) {
        if (!(send instanceof EntityPlayerMP)) return;
        EntityPlayerMP sender = (EntityPlayerMP) send;
        Map currentMap = Nailed.mapLoader.getMapFromWorld(sender.getServerForPlayer());
        if (args.length == 0) {
            throw new WrongUsageException("/teamleader <set:unset> <player>");
        }else if(args.length == 1){
            if(args[0].equals("unset") || args[0].equals("set")){
                throw new WrongUsageException("/teamleader " + args[0] + " <player>");
            }
        }else if(args.length == 2){
            if(args[0].equals("set")){
                Player player = Nailed.playerRegistry.getPlayer(args[1]);
                if(player == null) throw new CommandException("Player " + args[1] + " was not found");
                if(player.getTeam() == Team.UNKNOWN) throw new CommandException("Player " + args[1] + " does not have a team");
                player.getTeam().setTeamLeader(player);
                currentMap.sendMessageToAllPlayersWithoutPrefix(player.getGroup().getChatPrefix() + player.getChatFormattedName() + EnumColor.GREEN + " is now the leader of team " + player.getTeam().toString());
            }else if(args[0].equals("unset")){
                Player player = Nailed.playerRegistry.getPlayer(args[1]);
                if(player == null) throw new CommandException("Player " + args[1] + " was not found");
                if(player.getTeam() == Team.UNKNOWN) throw new CommandException("Player " + args[1] + " does not have a team");
                if(player.getTeam().getTeamLeader() != player) throw new CommandException("Player " + args[1] + " is not the teamleader of his team");
                player.getTeam().setTeamLeader(null);
                currentMap.sendMessageToAllPlayersWithoutPrefix(player.getGroup().getChatPrefix() + player.getChatFormattedName() + EnumColor.GOLD + " is no longer the leader of team " + player.getTeam().toString());
            }
        }else throw new WrongUsageException("/teamleader <set:unset> <player>");
    }

    @Override
    public List addTabCompletionOptions(ICommandSender send, String[] args) {
        if (!(send instanceof EntityPlayerMP)) return null;
        EntityPlayerMP sender = (EntityPlayerMP) send;
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "set", "unset");
        } else if (args.length == 2) {
            if (args[0].equals("set") || args[0].equals("unset")) {
                Player p = Nailed.playerRegistry.getPlayer(args[1]);
                if(p == null) return null;
                return getListOfStringsFromIterableMatchingLastWord(args, p.getTeam().getAllPlayerNames());
            }
        }
        return null;
    }
}
