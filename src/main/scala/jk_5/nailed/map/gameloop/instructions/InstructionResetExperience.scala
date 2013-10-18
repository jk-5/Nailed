package jk_5.nailed.map.gameloop.instructions

import jk_5.nailed.map.gameloop.{GameThread, IInstruction}

/**
 * No description given
 *
 * @author jk-5
 */
class InstructionResetExperience extends IInstruction {
  private var team: String = null
  def injectArguments(arguments: String) = this.team = arguments
  def execute(controller: GameThread) {
    val team = controller.getMap.getTeamManager.getTeam(this.team)
    if(team.isEmpty) return
    controller.getMap.getPlayers.filter(p => p.getTeam.eq(team.get) && p.getEntity.isDefined).map(_.getEntity.get).foreach(_.addExperienceLevel(-1000000))
  }
}
