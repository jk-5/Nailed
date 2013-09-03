package jk_5.nailed.map.gameloop.instructions;

import jk_5.nailed.Nailed;
import jk_5.nailed.map.gameloop.GameThread;
import jk_5.nailed.map.gameloop.IInstruction;

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
    public void execute(GameThread controller) throws InterruptedException {
        Nailed.statManager.triggerStat(this.stat);
    }
}
