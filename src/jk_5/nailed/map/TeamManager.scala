package jk_5.nailed.map

import scala.collection.mutable
import jk_5.nailed.team.Team
import net.minecraft.src.ScorePlayerTeam
import jk_5.nailed.util.EnumColor
import jk_5.nailed.config.helper.ConfigTag

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
    val teamlist = conf.getTag("teams").getSortedTagList
    if(teamlist == null) return
    val scoreboard = this.map.getScoreboard
    for(tag: ConfigTag <- teamlist){
      val team = new Team(tag.name)
      val color = EnumColor.valueOf(tag.getTag("color").getValue("white").toUpperCase)
      team.setName(tag.getTag("name").getValue(""))
      team.setColor(color)
      team.setMap(this.map)
      team.setFriendlyFireEnabled(tag.getTag("friendlyfire").getBooleanValue(false))
      this.teams.add(team)
      val scoreboardTeam = scoreboard.func_96527_f(tag.name)
      scoreboardTeam.func_96664_a(tag.getTag("name").getValue(""))
      scoreboardTeam.func_96660_a(team.friendlyFireEnabled)
      scoreboardTeam.func_98300_b(tag.getTag("frienlyinvisibles").getBooleanValue(true))
      scoreboardTeam.func_96666_b(color.toString)
      scoreboardTeam.func_96662_c(EnumColor.RESET.toString)
      team.setScoreboardTeam(scoreboardTeam)
    }
    this.createSpectatorTeam()
  }

  private def createSpectatorTeam(){
    this.spectatorTeam = this.map.getScoreboard.func_96527_f("spectator")
    this.spectatorTeam.func_96664_a("Spectator")
    this.spectatorTeam.func_96660_a(false)
    this.spectatorTeam.func_98300_b(true)
    this.spectatorTeam.func_96666_b(EnumColor.AQUA.toString)
    this.spectatorTeam.func_96662_c(EnumColor.RESET.toString)
  }

  @inline def getTeam(teamId: String) = this.teams.find(_.getTeamID == teamId)
  @inline def getTeamNames = this.teams.map(_.getTeamID)
  @inline def getTeams = this.teams
}
