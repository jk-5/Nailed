package jk_5.nailed.groups;

import jk_5.nailed.util.EnumColor;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public abstract class Group {

    public static Group player = new GroupPlayer();
    public static Group admin = new GroupAdmin();

    protected EnumColor nameColor;
    protected String name;

    public EnumColor getNameColor(){
        return this.nameColor;
    }

    public String getChatPrefix(){
        return "";
    }
}
