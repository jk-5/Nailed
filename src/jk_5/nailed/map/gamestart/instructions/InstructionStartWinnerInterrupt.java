package jk_5.nailed.map.gamestart.instructions;

import jk_5.nailed.map.gamestart.GameThread;
import jk_5.nailed.map.gamestart.IInstruction;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class InstructionStartWinnerInterrupt implements IInstruction {

    @Override
    public void injectArguments(String arguments) {

    }

    @Override
    public void execute(GameThread controller) throws InterruptedException {
        controller.setWinnerInterrupt(true);
    }
}
