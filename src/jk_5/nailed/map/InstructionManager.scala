package jk_5.nailed.map

import scala.collection.immutable
import java.util.concurrent.atomic.AtomicInteger
import jk_5.nailed.map.gameloop.instructions._
import jk_5.nailed.map.gameloop.IInstruction

/**
 * No description given
 *
 * @author jk-5
 */
object InstructionManager {
  private final val instructionMap = immutable.HashMap[String, Class[_]](
    "trigger" -> classOf[InstructionTrigger],
    "sleep" -> classOf[InstructionSleep],
    "watchunready" -> classOf[InstructionWatchUnready],
    "unwatchunready" -> classOf[InstructionUnwatchUnready],
    "countdown" -> classOf[InstructionCountdown],
    "setwinner" -> classOf[InstructionSetWinner],
    "startwinnerinterrupt" -> classOf[InstructionStartWinnerInterrupt],
    "enable" -> classOf[InstructionEnableStat],
    "disable" -> classOf[InstructionDisableStat],
    "setspawn" -> classOf[InstructionSetSpawnpoint],
    "resetspawn" -> classOf[InstructionResetSpawnpoint],
    "clearinventory" -> classOf[InstructionClearInventory],
    "setgamemode" -> classOf[InstructionSetGamemode],
    "moveteamspeak" -> classOf[InstructionMoveTeamspeak],
    "settime" -> classOf[InstructionSetTime],
    "setdifficulty" -> classOf[InstructionSetDifficulty],
    "clearexperience" -> classOf[InstructionResetExperience],
    "sethealth" -> classOf[InstructionSetHealth],
    "setfoodlevel" -> classOf[InstructionSetFoodLevel],
    "countup" -> classOf[InstructionCountUp]
  )
  def getInstruction(name: String): IInstruction = this.instructionMap.get(name).get.newInstance().asInstanceOf[IInstruction]
}
