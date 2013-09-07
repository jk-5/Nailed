package jk_5.nailed.event.player;

import jk_5.nailed.players.Player;
import net.minecraft.src.EntityPlayerMP;

/**
 * No description given
 *
 * @author jk-5
 */
public class PlayerChatEvent extends PlayerEvent {

    public String message;

    public PlayerChatEvent(Player player, String message){
        super(player);
        this.message = message;
    }
}
