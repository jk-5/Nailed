package jk_5.nailed.teams;

import com.google.common.collect.Maps;
import jk_5.nailed.Nailed;
import jk_5.nailed.players.Player;
import jk_5.nailed.util.EnumColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ScorePlayerTeam;
import net.minecraft.src.Scoreboard;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class TeamRegistry {

    private Map<String, Team> teams = Maps.newHashMap();
    private Map<String, Team> playerTeamMap = Maps.newHashMap();

    public static ScorePlayerTeam spectatorTeam;

    public void setupTeams() {
        Properties conf = Nailed.mapManager.getConfig();
        String teamsData = conf.getProperty("teams", null);
        if (teamsData == null) return;
        String teams[] = teamsData.split(",");
        Scoreboard s = this.getScoreboardFromWorldServer();
        this.spectatorTeam = s.func_96527_f("spectator");
        this.spectatorTeam.func_96664_a("Spectator");                   //teamname
        this.spectatorTeam.func_96660_a(false);                         //friendlyfire
        this.spectatorTeam.func_98300_b(true);                          //friendlyinvisibles
        this.spectatorTeam.func_96666_b(EnumColor.AQUA.toString());     //teamprefix
        this.spectatorTeam.func_96662_c(EnumColor.RESET.toString());    //teamsuffix
        for (String t : teams) {
            EnumColor color = EnumColor.valueOf(conf.getProperty("teams." + t.trim() + ".color", "white").toUpperCase());
            String name = conf.getProperty("teams." + t.trim() + ".name", t);
            Team team = new Team(name, t, color);
            this.teams.put(t, team);
            ScorePlayerTeam scoreboardTeam = s.func_96527_f(t);         //teamid
            scoreboardTeam.func_96664_a(name);                          //teamname
            scoreboardTeam.func_96660_a(false);                         //friendlyfire
            scoreboardTeam.func_98300_b(true);                          //friendlyinvisibles
            scoreboardTeam.func_96666_b(color.toString());              //teamprefix
            scoreboardTeam.func_96662_c(EnumColor.RESET.toString());    //teamsuffix
            team.scoreboardTeam = scoreboardTeam;
        }
    }

    private Scoreboard getScoreboardFromWorldServer() {
        return MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
    }

    public Team getTeamFromPlayer(String username) {
        if (!this.playerTeamMap.containsKey(username)) return Team.UNKNOWN;
        else return this.playerTeamMap.get(username);
    }

    public void setPlayerTeam(Player p, Team t) {
        Team current = this.playerTeamMap.get(p.getUsername());
        if (current != null) {
            this.playerTeamMap.remove(p.getUsername());
            this.getScoreboardFromWorldServer().func_96524_g(p.getUsername()); //leave
        }
        if (t.scoreboardTeam != null)
            this.getScoreboardFromWorldServer().func_96521_a(p.getUsername(), t.scoreboardTeam);
        this.playerTeamMap.put(p.getUsername(), t);
    }

    public Team getTeam(String teamId) {
        return this.teams.get(teamId);
    }

    public Set<String> getTeamNames() {
        return this.teams.keySet();
    }
}
