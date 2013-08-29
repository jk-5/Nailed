package jk_5.nailed.players;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class PlayerRegistry {

    private Map<String, Player> players = Maps.newHashMap();

    public Player getPlayer(String username) {
        return this.players.get(username);
    }

    public Collection<Player> getPlayers() {
        return this.players.values();
    }

    public Player getOrCreatePlayer(String username) {
        if (this.players.containsKey(username)) return this.players.get(username);
        else {
            final Player p = new Player(username);
            this.players.put(username, p);
            return p;
        }
    }

    public String formatPlayerListPlayer(String name) {
        return name;
        /*Player p = this.getPlayer(name);
        if(p == null) return name;
        return p.getGroup().getChatPrefix() + p.getTeam().getColor().toString() + p.getUsername() + EnumColor.RESET;*/
    }
}
