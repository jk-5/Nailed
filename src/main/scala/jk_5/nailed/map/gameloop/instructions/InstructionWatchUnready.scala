package jk_5.nailed.map.gameloop.instructions

import jk_5.nailed.map.gameloop.GameThread
import jk_5.nailed.map.gameloop.IInstruction

/**
 * No description given
 *
 * @author jk-5
 */
class InstructionWatchUnready extends IInstruction {
  def injectArguments(args: String) = {}
  def execute(controller: GameThread) = controller.setWatchUnready(watch = true)
}
