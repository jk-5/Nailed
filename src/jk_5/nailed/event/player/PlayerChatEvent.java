package jk_5.nailed.event.player;

import net.minecraft.src.EntityPlayerMP;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class PlayerChatEvent extends PlayerEvent {

    public EntityPlayerMP player;
    public String message;

    public PlayerChatEvent(EntityPlayerMP player, String message){
        this.player = player;
        this.message = message;
    }
}
