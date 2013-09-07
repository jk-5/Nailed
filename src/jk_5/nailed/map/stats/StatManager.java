package jk_5.nailed.map.stats;

import com.google.common.collect.Lists;
import net.minecraft.src.TileEntityCommandBlock;

import java.util.Iterator;
import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public class StatManager {

    private final List<CommandBlock> commandBlocks = Lists.newArrayList();

    public void updateCommandBlock(TileEntityCommandBlock commandBlock) {
        if (stripFormatting(commandBlock.getCommandSenderName()).startsWith("?")) {
            for (CommandBlock block : this.commandBlocks) {
                if (block.getTileEntity() == commandBlock) return;
            }
            this.commandBlocks.add(new CommandBlock(commandBlock));
        } else {
            Iterator<CommandBlock> it = this.commandBlocks.iterator();
            while (it.hasNext()) {
                CommandBlock block = it.next();
                if (block.getTileEntity() == commandBlock) {
                    it.remove();
                    return;
                }
            }
        }
    }

    /**
     * WARNING: this actually sleeps a short time! Use with care!
     */
    public void triggerStat(String stat) throws InterruptedException {
        this.enableStat(stat);
        Thread.sleep(300);
        this.disableStat(stat);
    }

    public void enableStat(String stat) {
        for (CommandBlock block : this.commandBlocks) {
            if (block.getListenerName().equalsIgnoreCase("?" + stat)) {
                block.setRedstoneOutput(15);
            }
        }
    }

    public void disableStat(String stat) {
        for (CommandBlock block : this.commandBlocks) {
            if (block.getListenerName().equalsIgnoreCase("?" + stat)) {
                block.setRedstoneOutput(0);
            }
        }
    }

    static String stripFormatting(String input) {
        return input.replaceAll("§[0-9a-fA-F]+", "");
    }
}
