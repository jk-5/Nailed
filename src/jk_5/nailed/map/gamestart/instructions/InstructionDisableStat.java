package jk_5.nailed.map.gamestart.instructions;

import jk_5.nailed.Nailed;
import jk_5.nailed.map.gamestart.GameThread;
import jk_5.nailed.map.gamestart.IInstruction;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class InstructionDisableStat implements IInstruction {

    private String stat;

    @Override
    public void injectArguments(String arguments) {
        this.stat = arguments;
    }

    @Override
    public void execute(GameThread controller) throws InterruptedException {
        Nailed.statManager.disableStat(this.stat);
    }
}
