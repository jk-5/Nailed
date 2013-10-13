package jk_5.nailed.command;

import net.minecraft.src.ICommand;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class CommandProxy implements ICommand {

    public int compareTo(Object o){
        return this.getCommandName().compareTo(((ICommand)o).getCommandName());
    }
}
