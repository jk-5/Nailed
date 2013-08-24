package jk_5.nailed.teams;

import jk_5.nailed.util.EnumColor;
import net.minecraft.src.ScorePlayerTeam;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class Team {

    public static Team UNKNOWN = new Team("Unknown Team", "unknown", EnumColor.GREY);

    private String name;
    private String teamId;
    private EnumColor color;
    public ScorePlayerTeam scoreboardTeam = null;

    public Team(String name, String teamId, EnumColor color){
        this.name = name;
        this.teamId = teamId;
        this.color = color;
    }

    public EnumColor getColor(){
        return this.color;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public String toString(){
        return String.format("%s%s%s", this.color.toString(), this.name, EnumColor.RESET);
    }

    public String toChatFormatString(){
        if(this == Team.UNKNOWN) return "";
        return String.format("%s* %s%s ", this.getColor().toString(), this.getName(), EnumColor.RESET);
    }
}
