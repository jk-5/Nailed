package jk_5.nailed.command;

import net.minecraft.command.ICommand;

/**
 * Because scala acts weird while overriding this method. Okay then.
 *
 * @author jk-5
 */
public abstract class CommandProxy implements ICommand {

    public int compareTo(Object o){
        return this.getCommandName().compareTo(((ICommand)o).getCommandName());
    }
}
