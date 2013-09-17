package jk_5.nailed.command;

import com.google.common.base.Joiner;
import jk_5.nailed.Nailed;
import jk_5.nailed.map.Map;
import jk_5.nailed.players.Player;
import jk_5.nailed.teams.Team;
import jk_5.nailed.util.EnumColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import java.util.List;

/**
 * No description given
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
        return 2;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        Player p = Nailed.playerRegistry.getPlayer(sender.getCommandSenderName());
        return p != null && p.getGroup().getGroupID().equalsIgnoreCase("admin") || p.getTeam().getTeamLeader() == p;
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "/team - Adds a player to a team";
    }

    @Override
    public void processCommand(ICommandSender send, String[] args) {
        if (!(send instanceof EntityPlayerMP)) return;
        EntityPlayerMP sender = (EntityPlayerMP) send;
        Map currentMap = Nailed.mapLoader.getMapFromWorld(sender.getServerForPlayer());
        Player senderPlayer = Nailed.playerRegistry.getPlayer(send.getCommandSenderName());
        if(senderPlayer == null) return;
        if (args.length == 0) {
            throw new WrongUsageException("/team <join:leave>");
        } else if (args.length == 1) {
            if (args[0].equals("join")) {
                if(senderPlayer.isAdmin()){
                    throw new WrongUsageException("/team join <player> <" + Joiner.on(':').join(currentMap.getTeamManager().getTeamNames()) + ">");
                }else{
                    throw new WrongUsageException("/team join <player>");
                }
            } else if (args[0].equals("leave")) {
                throw new WrongUsageException("/team leave <player>");
            }
        } else if (args.length == 2) {
            if (args[0].equals("join")) {
                Player p = Nailed.playerRegistry.getPlayer(args[1]);
                if (p == null) throw new CommandException("Player " + args[1] + " was not found");
                else{
                    if(senderPlayer.isTeamLeader()){
                        if(p.isTeamLeader()) throw new CommandException("Player " + p.getUsername() + " is teamleader of another team");
                        p.setTeam(senderPlayer.getTeam());
                        p.sendChatMessage(EnumColor.GREEN + "You have been added to the team " + p.getTeam().getColor() + p.getTeam().getName());
                        senderPlayer.sendChatMessage(EnumColor.GREEN + "Player " + p.getUsername() + " was added to the team " + p.getTeam().getColor() + p.getTeam().getName());
                    }else if(senderPlayer.isAdmin()) {
                        throw new WrongUsageException("/team join <player> <" + Joiner.on(':').join(currentMap.getTeamManager().getTeamNames()) + ">");
                    }
                }
            } else if (args[0].equals("leave")) {
                Player p = Nailed.playerRegistry.getPlayer(args[1]);
                if (p == null) throw new CommandException("Player " + args[1] + " was not found");
                else if(senderPlayer.isAdmin() || senderPlayer.isTeamLeader()){
                    if(senderPlayer.isTeamLeader() && p.getTeam() != senderPlayer.getTeam() && !senderPlayer.isAdmin()) throw new CommandException("Player " + p.getUsername() + " is not in your team!");
                    p.sendChatMessage(EnumColor.GREEN + "You have been removed from the team " + p.getTeam().getColor() + p.getTeam().getName());
                    sender.sendChatToPlayer(ChatMessageComponent.func_111066_d(EnumColor.GREEN + "Player " + p.getUsername() + " was removed from the team " + p.getTeam().getColor() + p.getTeam().getName()));
                    if(p.getTeam().getTeamLeader() == p){
                        p.getTeam().setTeamLeader(null);
                        currentMap.sendMessageToAllPlayersWithoutPrefix(p.getGroup().getChatPrefix() + p.getChatFormattedName() + EnumColor.GOLD + " is no longer the leader of team " + p.getTeam().toString());
                    }
                    p.setTeam(Team.UNKNOWN);
                }
            }
        } else if (args.length == 3) {
            if (args[0].equals("join") && senderPlayer.isAdmin()) {
                Player p = Nailed.playerRegistry.getPlayer(args[1]);
                Team t = currentMap.getTeamManager().getTeam(args[2]);
                if (p == null) throw new CommandException("Player " + args[1] + " was not found");
                if (t == null) throw new CommandException("Team " + args[2] + " was not found");
                else {
                    if(p.getTeam() != null){
                        if(p.getTeam().getTeamLeader() == p){
                            p.getTeam().setTeamLeader(null);
                            currentMap.sendMessageToAllPlayersWithoutPrefix(p.getGroup().getChatPrefix() + p.getChatFormattedName() + EnumColor.GOLD + " is no longer the leader of team " + p.getTeam().toString());
                        }
                    }
                    p.setTeam(t);
                    p.sendChatMessage(EnumColor.GREEN + "You have been added to the team " + p.getTeam().getColor() + p.getTeam().getName());
                    sender.sendChatToPlayer(ChatMessageComponent.func_111066_d(EnumColor.GREEN + "Player " + p.getUsername() + " was added to the team " + p.getTeam().getColor() + p.getTeam().getName()));
                }
            }else if(senderPlayer.isTeamLeader()) throw new WrongUsageException("/team <join:leave> <player>");
        } else throw new WrongUsageException("/team <join:leave>");
    }

    @Override
    public List addTabCompletionOptions(ICommandSender send, String[] args) {
        if (!(send instanceof EntityPlayerMP)) return null;
        EntityPlayerMP sender = (EntityPlayerMP) send;
        Map currentMap = Nailed.mapLoader.getMapFromWorld(sender.getServerForPlayer());
        Player senderPlayer = Nailed.playerRegistry.getPlayer(send.getCommandSenderName());
        if(senderPlayer == null) return null;
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "join", "leave");
        } else if (args.length == 2) {
            if (args[0].equals("join") || args[0].equals("leave")) {
                return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
            }
        } else if (args.length == 3) {
            if (args[0].equals("join") && senderPlayer.isAdmin()) {
                return getListOfStringsFromIterableMatchingLastWord(args, currentMap.getTeamManager().getTeamNames());
            }
        }
        return null;
    }
}
