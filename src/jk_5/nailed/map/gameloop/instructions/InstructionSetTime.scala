package jk_5.nailed.map.gameloop.instructions

import jk_5.nailed.map.gameloop.GameThread
import jk_5.nailed.map.gameloop.IInstruction

/**
 * No description given
 *
 * @author jk-5
 */
class InstructionSetTime extends IInstruction {
  private var time: Int = 0
  def injectArguments(arguments: String) = this.time = Integer.parseInt(arguments)
  def execute(controller: GameThread) = controller.getMap.getWorld.setWorldTime(time % 24000)
}
