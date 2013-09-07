package jk_5.nailed.map.gameloop.instructions;

import jk_5.nailed.map.gameloop.GameThread;
import jk_5.nailed.map.gameloop.IInstruction;

/**
 * No description given
 *
 * @author jk-5
 */
public class InstructionStopWinnerInterrupt implements IInstruction {

    @Override
    public void injectArguments(String arguments) {

    }

    @Override
    public void execute(GameThread controller) throws InterruptedException {
        controller.setWinnerInterrupt(false);
    }
}
