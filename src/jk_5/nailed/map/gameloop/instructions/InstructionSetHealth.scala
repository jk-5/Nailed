package jk_5.nailed.map.gameloop.instructions

import jk_5.nailed.map.gameloop.GameThread
import jk_5.nailed.map.gameloop.IInstruction

/**
 * No description given
 *
 * @author jk-5
 */
class InstructionSetHealth extends IInstruction {
  private var team: String = null
  private var health: Int = 0

  def injectArguments(arguments: String) {
    val data = arguments.split(" ", 2)
    this.team = data(0)
    this.health = Integer.parseInt(data(1))
  }

  def execute(controller: GameThread) {
    val team = controller.getMap.getTeamManager.getTeam(this.team)
    if(team.isEmpty) return
    controller.getMap.getPlayers.filter(p => p.getEntity.isDefined && p.getTeam.eq(team.get)).map(_.getEntity.get).foreach(_.setEntityHealth(this.health))
  }
}
