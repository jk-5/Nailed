package jk_5.nailed.map.gameloop.instructions

import jk_5.nailed.map.gameloop.{GameThread, IInstruction}
import jk_5.nailed.map.stats.StatManager

/**
 * No description given
 *
 * @author jk-5
 */
class InstructionDisableStat extends IInstruction {
  private var stat: String = null

  def injectArguments(arguments: String) = this.stat = arguments
  def execute(controller: GameThread) = StatManager.disableStat(this.stat)
}
