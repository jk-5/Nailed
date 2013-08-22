package jk_5.nailed.event.player;

import jk_5.nailed.players.Player;
import net.minecraft.src.EntityPlayerMP;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class PlayerJoinServerEvent extends PlayerEvent {

    public PlayerJoinServerEvent(EntityPlayerMP player){
        super(player);
    }
}
