package jk_5.nailed.map.gameloop.instructions

import jk_5.nailed.map.gameloop.GameThread
import jk_5.nailed.map.gameloop.IInstruction
import jk_5.nailed.map.stats.StatManager

/**
 * No description given
 *
 * @author jk-5
 */
class InstructionEnableStat extends IInstruction {
  private var stat: String = null

  def injectArguments(arguments: String) = this.stat = arguments
  def execute(controller: GameThread) = StatManager.enableStat(this.stat)
}
