package jk_5.nailed.players;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import jk_5.nailed.event.player.PlayerJoinServerEvent;
import jk_5.nailed.event.player.PlayerLeaveServerEvent;

import java.util.List;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class PlayerRegistry {

    private List<Player> playerList = Lists.newArrayList();

    @Subscribe
    public void onPlayerJoinServer(PlayerJoinServerEvent event){
        this.playerList.add(new Player(event.player.getCommandSenderName()));
    }

    @Subscribe
    public void onPlayerLeaveServer(PlayerLeaveServerEvent event){
        for(Player p : this.playerList){
            if(p.username.equals(event.player.getCommandSenderName())){
                this.playerList.remove(p);
            }
        }
    }
}
