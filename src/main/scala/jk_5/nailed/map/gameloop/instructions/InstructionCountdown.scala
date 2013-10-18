package jk_5.nailed.map.gameloop.instructions

import jk_5.nailed.map.gameloop.{TTimedInstruction, GameThread}
import jk_5.nailed.util.Utils

/**
 * Counts down a specific amount of time with the given message
 *
 * @author jk-5
 */
class InstructionCountdown extends TTimedInstruction {

  private var ticks: Int = 0
  private var message: String = null

  def injectArguments(arguments: String) {
    val args = arguments.split(" ", 2)
    this.message = args(1)
    val timedata = args(0).toLowerCase.trim
    if (timedata.endsWith("sec")) this.ticks = Integer.parseInt(timedata.substring(0, timedata.length - 3))
    else if (timedata.endsWith("min")) this.ticks = Integer.parseInt(timedata.substring(0, timedata.length - 3)) * 60
    else if (timedata.endsWith("hour")) this.ticks = Integer.parseInt(timedata.substring(0, timedata.length - 4)) * 3600
    else throw new RuntimeException("Unable to parse the countdown command properly")
  }

  def shouldContinue(controller: GameThread, ticks: Int): Boolean = {
    if ((this.ticks - ticks) == 60 || (this.ticks - ticks) == 30 || (this.ticks - ticks) == 20 || (this.ticks - ticks) == 10 || (this.ticks - ticks) <= 5 || ticks % 60 == 0) {
      controller.getMap.sendMessageToAllPlayers(message.format(Utils.formatSeconds(this.ticks - ticks)))
      if ((this.ticks - ticks) <= 5) {
        controller.getMap.getPlayers.foreach(_.playSound("note.harp", 1.5f, if ((this.ticks - ticks) == 0) 2 else 1))
      }
    }
    ticks >= this.ticks
  }
}
