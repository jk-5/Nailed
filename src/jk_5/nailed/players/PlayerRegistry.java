package jk_5.nailed.players;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import jk_5.nailed.event.player.PlayerJoinServerEvent;
import jk_5.nailed.event.player.PlayerLeaveServerEvent;
import jk_5.nailed.groups.Group;
import jk_5.nailed.util.EnumColor;

import java.util.List;
import java.util.Map;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class PlayerRegistry {

    private Map<String, Player> players = Maps.newHashMap();

    public Player getPlayer(String username){
        return this.players.get(username);
    }

    public Player getOrCreatePlayer(String username){
        if(this.players.containsKey(username)) return this.players.get(username);
        else{
            final Player p = new Player(username);
            this.players.put(username, p);
            return p;
        }
    }

    public String formatPlayerListPlayer(String name){
        return name;
        /*Player p = this.getPlayer(name);
        if(p == null) return name;
        return p.getGroup().getChatPrefix() + p.getTeam().getColor().toString() + p.getUsername() + EnumColor.RESET;*/
    }
}
