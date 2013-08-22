package jk_5.nailed.event.player;

import net.minecraft.src.EntityPlayerMP;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class PlayerLeaveServerEvent extends PlayerEvent {

    public EntityPlayerMP player;

    public PlayerLeaveServerEvent(EntityPlayerMP player){
        this.player = player;
    }
}
