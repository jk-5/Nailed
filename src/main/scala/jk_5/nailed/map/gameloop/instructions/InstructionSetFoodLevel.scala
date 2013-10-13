package jk_5.nailed.map.gameloop.instructions

import jk_5.nailed.map.gameloop.GameThread
import jk_5.nailed.map.gameloop.IInstruction

/**
 * No description given
 *
 * @author jk-5
 */
class InstructionSetFoodLevel extends IInstruction {
  private var team: String = null
  private var foodLevel: Int = 0

  def injectArguments(arguments: String) {
    val data = arguments.split(" ", 2)
    this.team = data(0)
    this.foodLevel = Integer.parseInt(data(1))
  }

  def execute(controller: GameThread) {
    val team = controller.getMap.getTeamManager.getTeam(this.team)
    if(team.isEmpty) return
    controller.getMap.getPlayers.filter(p => p.getTeam.eq(team.get) && p.getEntity.isDefined).map(_.getEntity.get.getFoodStats).foreach(s => s.addStats(this.foodLevel - s.getFoodLevel, 0))
  }
}
