package jk_5.nailed.map;

import com.google.common.collect.Maps;
import jk_5.nailed.config.helper.ConfigFile;
import jk_5.nailed.config.helper.ConfigTag;
import jk_5.nailed.players.Player;
import jk_5.nailed.teams.Team;
import jk_5.nailed.util.EnumColor;
import net.minecraft.src.ScorePlayerTeam;
import net.minecraft.src.Scoreboard;

import java.util.List;
import java.util.Set;

/**
 * No description given
 *
 * @author jk-5
 */
public class TeamManager {

    private final Map map;
    private java.util.Map<String, Team> teams = Maps.newHashMap();
    private java.util.Map<String, Team> playerTeamMap = Maps.newHashMap();

    public ScorePlayerTeam spectatorTeam;

    public TeamManager(Map map) {
        this.map = map;
    }

    public void setupTeams() {
        ConfigFile conf = this.map.getMappack().getConfig();
        List<ConfigTag> teams = conf.getTag("teams").getSortedTagList();
        if (teams == null) return;
        Scoreboard s = this.map.getHandle().getScoreboard();
        for (ConfigTag tag : teams) {
            EnumColor color = EnumColor.valueOf(tag.getTag("color").getValue("white").toUpperCase());
            String name = tag.getTag("name").getValue("");
            Team team = new Team(name, tag.name(), color);
            this.teams.put(tag.name(), team);
            ScorePlayerTeam scoreboardTeam = s.func_96527_f(tag.name());                            //teamid
            scoreboardTeam.func_96664_a(name);                                                      //teamname
            scoreboardTeam.func_96660_a(tag.getTag("frienlyfire").getBooleanValue(false));          //friendlyfire
            scoreboardTeam.func_98300_b(tag.getTag("friendlyinvisibles").getBooleanValue(true));    //friendlyinvisibles
            scoreboardTeam.func_96666_b(color.toString());                                          //teamprefix
            scoreboardTeam.func_96662_c(EnumColor.RESET.toString());                                //teamsuffix
            team.scoreboardTeam = scoreboardTeam;
        }
        this.createSpectatorTeam();
    }

    private void createSpectatorTeam() {
        Scoreboard s = this.map.getHandle().getScoreboard();
        spectatorTeam = s.func_96527_f("spectator");
        spectatorTeam.func_96664_a("Spectator");                   //teamname
        spectatorTeam.func_96660_a(false);                         //friendlyfire
        spectatorTeam.func_98300_b(true);                          //friendlyinvisibles
        spectatorTeam.func_96666_b(EnumColor.AQUA.toString());     //teamprefix
        spectatorTeam.func_96662_c(EnumColor.RESET.toString());    //teamsuffix
    }

    public Team getTeamFromPlayer(Player player) {
        if (!this.playerTeamMap.containsKey(player.getUsername())) return Team.UNKNOWN;
        else return this.playerTeamMap.get(player.getUsername());
    }

    public void setPlayerTeam(Player p, Team t) {
        Team current = this.playerTeamMap.get(p.getUsername());
        if (current != null) {
            this.playerTeamMap.remove(p.getUsername());
            this.map.getHandle().getScoreboard().func_96524_g(p.getUsername()); //leave
        }
        if (t.scoreboardTeam != null)
            this.map.getHandle().getScoreboard().func_96521_a(p.getUsername(), t.scoreboardTeam);
        this.playerTeamMap.put(p.getUsername(), t);
    }

    public Team getTeam(String teamId) {
        return this.teams.get(teamId);
    }

    public Set<String> getTeamNames() {
        return this.teams.keySet();
    }
}
