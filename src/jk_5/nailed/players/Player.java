package jk_5.nailed.players;

import jk_5.nailed.groups.Group;
import jk_5.nailed.util.EnumColor;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class Player {

    private String username;
    private Group group;

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

    public String formatName(){
        return this.group.getNameColor() + this.getUsername() + EnumColor.RESET.toString();
    }
}
