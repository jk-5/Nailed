package jk_5.nailed.map

import scala.collection.mutable
import jk_5.nailed.team.Team
import net.minecraft.src.ScorePlayerTeam
import jk_5.nailed.util.EnumColor

/**
 * No description given
 *
 * @author jk-5
 */
class TeamManager(private final val map: Map) {

  private final val teams = new mutable.HashSet[Team]()
  private var spectatorTeam: ScorePlayerTeam = _

  def setupTeams(){
    val conf = this.map.getMappack.getConfig
    val teams = conf.getTag("teams").getSortedTagList
    if(teams == null) return
    val scoreboard = this.map.getWorld.getScoreboard
    for(tag <- teams){
      val color = EnumColor.valueOf(tag.)
    }
  }

  public void setupTeams() {
    ConfigFile conf = this.map.getMappack().getConfig();
    List<ConfigTag> teams = conf.getTag("teams").getSortedTagList();
    if (teams == null) return;
    Scoreboard s = this.map.getWorld().getScoreboard();
    for (ConfigTag tag : teams) {
      EnumColor color = EnumColor.valueOf(tag.getTag("color").getValue("white").toUpperCase());
      String name = tag.getTag("name").getValue("");
      Team team = new Team(name, tag.name(), color);
      team.setMap(this.map);
      team.setFriendlyFireEnabled(tag.getTag("frienlyfire").getBooleanValue(false));
      this.teams.add(team);
      ScorePlayerTeam scoreboardTeam = s.func_96527_f(tag.name());                            //teamid
      scoreboardTeam.func_96664_a(name);                                                      //teamname
      scoreboardTeam.func_96660_a(team.friendlyFireEnabled());                                //friendlyfire
      scoreboardTeam.func_98300_b(tag.getTag("friendlyinvisibles").getBooleanValue(true));    //friendlyinvisibles
      scoreboardTeam.func_96666_b(color.toString());                                          //teamprefix
      scoreboardTeam.func_96662_c(EnumColor.RESET.toString());                                //teamsuffix
      team.scoreboardTeam = scoreboardTeam;
    }
    this.createSpectatorTeam();
  }

  private void createSpectatorTeam() {
    Scoreboard s = this.map.getWorld().getScoreboard();
    spectatorTeam = s.func_96527_f("spectator");
    spectatorTeam.func_96664_a("Spectator");                   //teamname
    spectatorTeam.func_96660_a(false);                         //friendlyfire
    spectatorTeam.func_98300_b(true);                          //friendlyinvisibles
    spectatorTeam.func_96666_b(EnumColor.AQUA.toString());     //teamprefix
    spectatorTeam.func_96662_c(EnumColor.RESET.toString());    //teamsuffix
  }

  public Team getTeam(String teamId) {
    for (Team team : this.teams) {
      if (team.getTeamID().equals(teamId)) {
        return team;
      }
    }
    return null;
  }

  public Set<String> getTeamNames() {
    Set<String> names = Sets.newHashSet();
    for (Team team : this.teams) {
      names.add(team.getTeamID());
    }
    return names;
  }

  public Set<Team> getTeams(){
    return this.teams;
  }
}
