package jk_5.nailed.groups;

import jk_5.nailed.Nailed;
import jk_5.nailed.util.EnumColor;
import net.minecraft.src.EntityPlayer;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public abstract class Group {

    protected EnumColor nameColor;
    protected String name;

    public String getGroupID() {
        return Nailed.groupRegistry.groups.inverse().get(this);
    }

    public EnumColor getNameColor() {
        return this.nameColor;
    }

    public String getChatPrefix() {
        return "";
    }

    public String getName() {
        return this.name;
    }

    public void onPlayerJoin(EntityPlayer player) {

    }

    public void onPlayerLeave(EntityPlayer player) {

    }
}
