package jk_5.nailed.event.player;

import jk_5.nailed.players.Player;
import net.minecraft.src.EntityPlayerMP;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class PlayerLeaveServerEvent extends PlayerEvent {

    public PlayerLeaveServerEvent(Player player){
        super(player);
    }
}
