package jk_5.nailed.map.gameloop.instructions

import jk_5.nailed.map.gameloop.GameThread
import jk_5.nailed.map.gameloop.IInstruction
import net.minecraft.util.ChunkCoordinates

/**
 * No description given
 *
 * @author jk-5
 */
class InstructionSetSpawnpoint extends IInstruction {
  private var coordinates: ChunkCoordinates = null
  private var team: String = null

  def injectArguments(arguments: String) {
    val data = arguments.split(" ", 4)
    this.coordinates = new ChunkCoordinates(data(1).toInt, data(2).toInt, data(3).toInt)
    this.team = data(0)
  }
  def execute(controller: GameThread) = controller.getMap.getTeamManager.getTeam(this.team).foreach(_.setSpawnpoint(this.coordinates))
}
