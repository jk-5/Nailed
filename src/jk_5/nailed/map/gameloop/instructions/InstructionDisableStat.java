package jk_5.nailed.map.gameloop.instructions;

import jk_5.nailed.Nailed;
import jk_5.nailed.map.gameloop.GameThread;
import jk_5.nailed.map.gameloop.IInstruction;

/**
 * No description given
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
