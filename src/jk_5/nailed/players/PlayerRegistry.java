package jk_5.nailed.players;

import com.google.common.collect.Maps;
import jk_5.nailed.Nailed;

import java.util.Collection;
import java.util.Map;

/**
 * No description given
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
            p.setCurrentMap(Nailed.mapLoader.getLobby());
            return p;
        }
    }
}
