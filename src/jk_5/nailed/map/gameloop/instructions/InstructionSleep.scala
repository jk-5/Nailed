package jk_5.nailed.map.gameloop.instructions

import jk_5.nailed.map.gameloop.GameThread
import jk_5.nailed.map.gameloop.IInstruction

/**
 * No description given
 *
 * @author jk-5
 */
class InstructionSleep extends IInstruction {
  private var delay: Long = 0L
  def injectArguments(args: String) = this.delay = args.toLong
  def execute(controller: GameThread) = Thread.sleep(this.delay)
}
