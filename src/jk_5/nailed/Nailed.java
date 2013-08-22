package jk_5.nailed;

import com.google.common.eventbus.EventBus;
import jk_5.nailed.players.PlayerRegistry;
import jk_5.nailed.teams.TeamRegistry;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class Nailed {
    public static final EventBus eventBus = new EventBus();
    public static final TeamRegistry teamRegistry = new TeamRegistry();
    public static final PlayerRegistry playerRegistry = new PlayerRegistry();

    public static void init(){
        eventBus.register(new NailedEventListener());
        eventBus.register(playerRegistry);
    }
}
