package jk_5.nailed.map.gameloop.instructions

import jk_5.nailed.map.gameloop.GameThread
import jk_5.nailed.map.gameloop.IInstruction

/**
 * No description given
 *
 * @author jk-5
 */
class InstructionSetWinner extends IInstruction {
  private var winner: String = null
  def injectArguments(arguments: String) = this.winner = arguments
  def execute(controller: GameThread) = controller.getMap.getTeamManager.getTeam(this.winner).foreach(t=>controller.setWinner(t))
}
