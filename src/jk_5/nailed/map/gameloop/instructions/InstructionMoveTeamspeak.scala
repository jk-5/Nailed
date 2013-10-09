package jk_5.nailed.map.gameloop.instructions

import jk_5.nailed.map.gameloop.GameThread
import jk_5.nailed.map.gameloop.IInstruction
import jk_5.nailed.teamspeak3.TeamspeakManager

/**
 * No description given
 *
 * @author jk-5
 */
class InstructionMoveTeamspeak extends IInstruction {
  private var team: String = null
  private var channelID: Int = 0

  def injectArguments(arguments: String) {
    val data = arguments.split(" ", 2)
    this.team = data(0)
    this.channelID = Integer.parseInt(data(1))
  }

  def execute(controller: GameThread) {
    val team = controller.getMap.getTeamManager.getTeam(this.team)
    if(team.isEmpty) return
    controller.getMap.getPlayers.filter(p => p.getTeam.eq(team.get) && p.getTeamspeakName != null).map(p => TeamspeakManager.getClientForUser(p.getTeamspeakName).get).foreach(ts => TeamspeakManager.moveClientToChannel(ts, this.channelID))
  }
}
