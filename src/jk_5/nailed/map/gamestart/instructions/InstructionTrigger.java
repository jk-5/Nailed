package jk_5.nailed.map.gamestart.instructions;

import jk_5.nailed.Nailed;
import jk_5.nailed.map.gamestart.GameStartupThread;
import jk_5.nailed.map.gamestart.IInstruction;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class InstructionTrigger implements IInstruction {

    private String stat;

    @Override
    public void injectArguments(String arguments) {
        this.stat = arguments;
    }

    @Override
    public void execute(GameStartupThread controller) throws InterruptedException {
        Nailed.statManager.triggerStat(this.stat);
    }
}
