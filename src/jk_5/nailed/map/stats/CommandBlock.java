package jk_5.nailed.map.stats;

import net.minecraft.src.Block;
import net.minecraft.src.TileEntityCommandBlock;

/**
 * Class that will be made for every special command block in the map
 *
 * @author jk-5
 */
public class CommandBlock {

    private final String listenerName;
    private final TileEntityCommandBlock tileEntity;

    public CommandBlock(TileEntityCommandBlock te) {
        this.tileEntity = te;
        this.listenerName = StatManager.stripFormatting(this.tileEntity.getCommandSenderName());
    }

    public void setRedstoneOutput(int strength) {
        this.tileEntity.func_96102_a(strength);
        this.tileEntity.getWorldObj().func_96440_m(this.tileEntity.xCoord, this.tileEntity.yCoord, this.tileEntity.zCoord, Block.commandBlock.blockID);
    }

    public TileEntityCommandBlock getTileEntity() {
        return this.tileEntity;
    }

    public String getListenerName() {
        return this.listenerName;
    }
}
