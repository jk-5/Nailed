package jk_5.nailed.players;

import jk_5.nailed.Nailed;
import jk_5.nailed.groups.Group;
import jk_5.nailed.teams.Team;
import jk_5.nailed.util.EnumColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ChatMessageComponent;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class Player {

    private String username;
    private Group group = Nailed.groupRegistry.getDefaultGroup();

    public Player(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public Group getGroup(){
        return this.group;
    }

    public void setGroup(Group group){
        this.group = group;
    }

    public Team getTeam(){
        return Nailed.teamRegistry.getTeamFromPlayer(username);
    }

    public String getChatFormattedName(){
        return this.group.getNameColor() + this.getUsername() + EnumColor.RESET.toString();
    }

    public String formatChatMessage(String message){
        return String.format("%s%s%s[%s%s] %s", this.getTeam().toChatFormatString(), this.getGroup().getChatPrefix(), EnumColor.GREY, this.getChatFormattedName(), EnumColor.GREY, message);
    }

    public void sendChatMessage(String message){
        MinecraftServer.getServer().getConfigurationManager().getPlayerEntity(this.username).sendChatToPlayer(ChatMessageComponent.func_111066_d(message));
    }
}
