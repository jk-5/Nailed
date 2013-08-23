package jk_5.nailed.groups;

import jk_5.nailed.util.EnumColor;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class GroupAdmin extends Group {

    public GroupAdmin(){
        this.name = "Admin";
        this.nameColor = EnumColor.GREY;
    }

    @Override
    public String getChatPrefix(){
        return EnumColor.GREEN + "[Admin] " + EnumColor.RESET;
    }
}
