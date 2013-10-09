package jk_5.nailed.map.gameloop.instructions

import jk_5.nailed.map.gameloop.GameThread
import jk_5.nailed.map.gameloop.IInstruction
import net.minecraft.src.WorldServer

/**
 * No description given
 *
 * @author jk-5
 */
class InstructionSetDifficulty extends IInstruction {
  private var difficulty: Int = 0
  def injectArguments(arguments: String) = this.difficulty = Integer.parseInt(arguments)
  def execute(controller: GameThread) {
    val server: WorldServer = controller.getMap.getWorld
    server.difficultySetting = this.difficulty
    server.setAllowedSpawnTypes(controller.getMap.getMappack.shouldSpawnHostileMobs && this.difficulty > 0, controller.getMap.getMappack.shouldSpawnFriendlyMobs)
  }
}
