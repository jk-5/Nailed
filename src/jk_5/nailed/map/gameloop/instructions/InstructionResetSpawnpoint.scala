package jk_5.nailed.map.gameloop.instructions

import jk_5.nailed.map.gameloop.GameThread
import jk_5.nailed.map.gameloop.IInstruction

/**
 * No description given
 *
 * @author jk-5
 */
class InstructionResetSpawnpoint extends IInstruction {
  private var team: String = null
  def injectArguments(arguments: String) = this.team = arguments
  def execute(controller: GameThread) = controller.getMap.getTeamManager.getTeam(this.team).foreach(_.setSpawnpoint(null))
}
