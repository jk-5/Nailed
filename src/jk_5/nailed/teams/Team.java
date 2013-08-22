package jk_5.nailed.teams;

import jk_5.nailed.util.EnumColor;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class Team {

    public static Team UNKNOWN = new Team("Unknown Team", EnumColor.GREY);

    private String name;
    private EnumColor color;

    public Team(String name, EnumColor color){
        this.name = name;
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
}
