package jk_5.nailed.map.gameloop.instructions

import jk_5.nailed.map.gameloop.GameThread
import jk_5.nailed.map.gameloop.IInstruction

/**
 * No description given
 *
 * @author jk-5
 */
class InstructionClearInventory extends IInstruction {

  private var team: String = null

  def injectArguments(arguments: String) = this.team = arguments

  def execute(controller: GameThread) {
    val team = controller.getMap.getTeamManager.getTeam(this.team)
    controller.getMap.getPlayers.filter(_.getEntity.isDefined).map(_.getEntity.get).foreach(p => {
      p.inventory.clearInventory(-1, -1)
      p.inventoryContainer.detectAndSendChanges()
      if(!p.capabilities.isCreativeMode) p.updateHeldItem()
    })
  }
}
