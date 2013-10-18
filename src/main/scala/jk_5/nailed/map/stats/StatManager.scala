package jk_5.nailed.map.stats

import scala.collection.mutable.ArrayBuffer
import net.minecraft.tileentity.TileEntityCommandBlock

/**
 * No description given
 *
 * @author jk-5
 */
object StatManager {
  private final val commandBlocks = ArrayBuffer[CommandBlock]()

  def updateCommandBlock(block: TileEntityCommandBlock){
    if(this.stripFormatting(block.getCommandSenderName).startsWith("?")){
      if(!this.commandBlocks.exists(_.getTileEntity == block)){
        this.commandBlocks += new CommandBlock(block)
      }
    }else this.commandBlocks.filter(_.getTileEntity == block).foreach(b => this.commandBlocks -= b)
  }

  /**
   * WARNING: this actually sleeps a short time! Use with care!
   */
  def triggerStat(stat: String){
    this.enableStat(stat)
    Thread.sleep(300)
    this.disableStat(stat)
  }
  def enableStat(stat: String) = this.commandBlocks.filter(_.getListenerName.equalsIgnoreCase("?" + stat)).foreach(_.setRedstoneOutput(15))
  def disableStat(stat: String) = this.commandBlocks.filter(_.getListenerName.equalsIgnoreCase("?" + stat)).foreach(_.setRedstoneOutput(0))
  @inline def stripFormatting(input: String) = input.replaceAll("§[0-9a-fA-F]+", "")
}
