package jk_5.nailed.event.player;

import jk_5.nailed.players.Player;
import net.minecraft.src.EntityPlayerMP;

/**
 * No description given
 *
 * @author jk-5
 */
public class PlayerJoinServerEvent extends PlayerEvent {

    public PlayerJoinServerEvent(Player player){
        super(player);
    }
}
