package jk_5.nailed.teams;

import com.google.common.collect.Maps;
import jk_5.nailed.util.EnumColor;

import java.util.Map;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class TeamRegistry {

    private Map<String, Team> playerTeamMap = Maps.newHashMap();

    public TeamRegistry(){
        this.playerTeamMap.put("Clank26", new Team("Team Red", EnumColor.RED));
    }

    public Team getTeamFromPlayer(String username){
        if(!this.playerTeamMap.containsKey(username)){
            return Team.UNKNOWN;
        }else{
            return this.playerTeamMap.get(username);
        }
    }
}
