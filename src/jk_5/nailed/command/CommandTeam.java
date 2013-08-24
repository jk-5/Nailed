package jk_5.nailed.command;

import com.google.common.base.Joiner;
import jk_5.nailed.Nailed;
import jk_5.nailed.groups.Group;
import jk_5.nailed.players.Player;
import jk_5.nailed.teams.*;
import jk_5.nailed.teams.Team;
import jk_5.nailed.util.EnumColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import java.util.List;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class CommandTeam extends CommandBase {

    @Override
    public String getCommandName() {
        return "team";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "/team - Adds a player to a team";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length == 0){
            throw new WrongUsageException("/team <join:leave>");
        }else if(args.length == 1){
            if(args[0].equals("join")){
                throw new WrongUsageException("/team join <player> <" + Joiner.on(':').join(Nailed.teamRegistry.getTeamNames()).toString() + ">");
            }else if(args[0].equals("leave")){
                throw new WrongUsageException("/team leave <player>");
            }
        }else if(args.length == 2){
            if(args[0].equals("join")){
                Player p = Nailed.playerRegistry.getPlayer(args[1]);
                if(p == null) throw new CommandException("Player " + args[1] + " was not found");
                else throw new WrongUsageException("/team join <player> <" + Joiner.on(':').join(Nailed.teamRegistry.getTeamNames()).toString() + ">");
            }else if(args[0].equals("leave")){
                Player p = Nailed.playerRegistry.getPlayer(args[1]);
                if(p == null) throw new CommandException("Player " + args[1] + " was not found");
                else{
                    p.sendChatMessage(EnumColor.GREEN + "You have been removed from the team " + p.getTeam().getColor() + p.getTeam().getName());
                    sender.sendChatToPlayer(ChatMessageComponent.func_111066_d(EnumColor.GREEN + "Player " + p.getUsername() + " was removed from the team " + p.getTeam().getColor() + p.getTeam().getName()));
                    Nailed.teamRegistry.setPlayerTeam(p, Team.UNKNOWN);
                }
            }
        }else if(args.length == 3){
            if(args[0].equals("join")){
                Player p = Nailed.playerRegistry.getPlayer(args[1]);
                Team t = Nailed.teamRegistry.getTeam(args[2]);
                if(p == null) throw new CommandException("Player " + args[1] + " was not found");
                if(t == null) throw new CommandException("Team " + args[2] + " was not found");
                else{
                    Nailed.teamRegistry.setPlayerTeam(p, t);
                    p.sendChatMessage(EnumColor.GREEN + "You have been added to the team " + p.getTeam().getColor() + p.getTeam().getName());
                    sender.sendChatToPlayer(ChatMessageComponent.func_111066_d(EnumColor.GREEN + "Player " + p.getUsername() + " was added to the team " + p.getTeam().getColor() + p.getTeam().getName()));
                }
            }
        }else throw new WrongUsageException("/team <join:leave>");
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 1){
            return getListOfStringsMatchingLastWord(args, new String[]{"join", "leave"});
        }else if(args.length == 2){
            if(args[0].equals("join") || args[0].equals("leave")){
                return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
            }
        }else if(args.length == 3){
            if(args[0].equals("join")){
                return getListOfStringsFromIterableMatchingLastWord(args, Nailed.teamRegistry.getTeamNames());
            }
        }
        return null;
    }
}