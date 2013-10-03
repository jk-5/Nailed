package jk_5.nailed.map.gameloop.instructions

import jk_5.nailed.map.gameloop.{TTimedInstruction, GameThread}
import jk_5.nailed.util.Utils

/**
 * No description given
 *
 * @author jk-5
 */
class InstructionCountUp extends TTimedInstruction {

  private var message: String = null

  def injectArguments(arguments: String) = this.message = arguments

  def shouldContinue(controller: GameThread, ticks: Int): Boolean = {
    if (ticks % 60 == 0) controller.getMap.sendMessageToAllPlayers(message.format(Utils.formatSeconds(ticks)))
    true
  }
}
