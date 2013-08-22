package jk_5.nailed.groups;

import jk_5.nailed.util.EnumColor;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class Group {

    private EnumColor nameColor;
    private String name;

    public Group(String name){
        this.name = name;
    }

    public EnumColor getNameColor(){
        return this.nameColor;
    }
}
