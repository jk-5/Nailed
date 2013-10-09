package jk_5.nailed.map

import scala.collection.{JavaConversions, mutable}
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
    val teamlist = conf.getTag("teams").getSortedTagList
    if(teamlist == null) return
    val scoreboard = this.map.getScoreboard
    for(tag <- JavaConversions.collectionAsScalaIterable(teamlist)){
      val team = new Team(tag.name)
      val color = EnumColor.valueOf(tag.getTag("color").getValue("white").toUpperCase)
      team.setName(tag.getTag("name").getValue(""))
      team.setColor(color)
      team.setMap(this.map)
      team.setFriendlyFireEnabled(tag.getTag("friendlyfire").getBooleanValue(default = false))
      this.teams.add(team)
      val scoreboardTeam = scoreboard.createTeam(tag.name)
      scoreboardTeam.setTeamName(tag.getTag("name").getValue(""))
      scoreboardTeam.setAllowFriendlyFire(team.friendlyFireEnabled)
      scoreboardTeam.setSeeFriendlyInvisiblesEnabled(tag.getTag("frienlyinvisibles").getBooleanValue(default = true))
      scoreboardTeam.setNamePrefix(color.toString)
      scoreboardTeam.setNameSuffix(EnumColor.RESET.toString)
      team.setScoreboardTeam(scoreboardTeam)
    }
    this.createSpectatorTeam()
  }

  private def createSpectatorTeam(){
    this.spectatorTeam = this.map.getScoreboard.createTeam("spectator")
    this.spectatorTeam.setTeamName("Spectator")
    this.spectatorTeam.setAllowFriendlyFire(false)
    this.spectatorTeam.setSeeFriendlyInvisiblesEnabled(true)
    this.spectatorTeam.setNamePrefix(EnumColor.AQUA.toString)
    this.spectatorTeam.setNameSuffix(EnumColor.RESET.toString)
  }

  @inline def getTeam(teamId: String) = this.teams.find(_.getTeamID == teamId)
  @inline def getTeamNames = this.teams.map(_.getTeamID)
  @inline def getTeams = this.teams
  @inline def getSpectatorTeam = this.spectatorTeam
}
