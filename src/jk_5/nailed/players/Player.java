package jk_5.nailed.players;

import jk_5.nailed.Nailed;
import jk_5.nailed.groups.Group;
import jk_5.nailed.teams.Team;
import jk_5.nailed.util.EnumColor;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class Player {

    private String username;
    private Group group = Group.player;
    private Team team = Team.UNKNOWN;

    public Player(String username){
        this.username = username;
        this.team = Nailed.teamRegistry.getTeamFromPlayer(username);
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
        return this.team;
    }

    public void setTeam(Team team){
        this.team = team;
    }

    public String getChatFormattedName(){
        return this.group.getNameColor() + this.getUsername() + EnumColor.RESET.toString();
    }

    public String formatChatMessage(String message){
        return String.format("%s%s%s[%s%s] %s", this.team.toChatFormatString(), this.getGroup().getChatPrefix(), EnumColor.GREY, this.getChatFormattedName(), EnumColor.GREY, message);
    }
}
