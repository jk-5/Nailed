package jk_5.nailed.map.gamestart.instructions;

import jk_5.nailed.map.gamestart.GameStartupThread;
import jk_5.nailed.map.gamestart.IInstruction;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class InstructionStopWinnerInterrupt implements IInstruction {

    @Override
    public void injectArguments(String arguments) {

    }

    @Override
    public void execute(GameStartupThread controller) throws InterruptedException {
        controller.setWinnerInterrupt(false);
    }
}
