package jk_5.nailed.command;

import jk_5.nailed.Nailed;
import jk_5.nailed.players.Player;
import jk_5.nailed.teamspeak3.TeamspeakClient;
import net.minecraft.src.*;

import java.util.Arrays;
import java.util.List;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class CommandTeamspeak extends CommandBase {

    @Override
    public String getCommandName() {
        return "teamspeak";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("ts");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender instanceof EntityPlayerMP;
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "/teamspeak - Teamspeak stuff";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            throw new WrongUsageException("/teamspeak <setname> <username>");
        }else if(args.length == 1){
            if(args[0].equals("setname")){
                throw new WrongUsageException("/teamspeak <setname> <username>");
            }
        }else if(args.length == 2){
            if(args[0].equals("setname")){
                Player p = Nailed.playerRegistry.getPlayer(sender.getCommandSenderName());
                if(p == null) throw new CommandException("You are nobody!");
                TeamspeakClient ts = Nailed.teamspeak.getClientForUser(args[1]);
                if(ts == null) throw new CommandException("The name you entered is not online on teamspeak!");
                p.setTeamspeakClient(ts);
            }
        }else throw new WrongUsageException("/teamspeak <setname> <username>");
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "setname");
        } else if (args.length == 2) {
            if (args[0].equals("setname")) {
                return getListOfStringsFromIterableMatchingLastWord(args, Nailed.teamspeak.getNicknames());
            }
        }
        return null;
    }
}
