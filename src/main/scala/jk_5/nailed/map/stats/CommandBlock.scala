package jk_5.nailed.map.stats

import net.minecraft.tileentity.TileEntityCommandBlock
import net.minecraft.block.Block

/**
 * No description given
 *
 * @author jk-5
 */
class CommandBlock(private final val te: TileEntityCommandBlock) {
  private final val listenerName = StatManager.stripFormatting(this.te.getCommandSenderName)

  def setRedstoneOutput(strength: Int) {
    this.te.setSignalStrength(strength)
    this.te.getWorldObj.func_96440_m(this.te.xCoord, this.te.yCoord, this.te.zCoord, Block.commandBlock.blockID)
  }
  @inline def getTileEntity = this.te
  @inline def getListenerName = this.listenerName
}
