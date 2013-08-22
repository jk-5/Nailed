package jk_5.nailed.event.player;

import jk_5.nailed.event.NailedEvent;
import jk_5.nailed.players.Player;
import net.minecraft.src.EntityPlayerMP;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class PlayerEvent extends NailedEvent {

    public Player playerInst;
    public EntityPlayerMP player;

    public PlayerEvent(EntityPlayerMP entity){
        this.player = entity;
    }
}
