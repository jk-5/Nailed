package jk_5.nailed.map.gameloop.instructions;

import jk_5.nailed.map.gameloop.GameThread;
import jk_5.nailed.map.gameloop.IInstruction;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class InstructionWatchUnready implements IInstruction {

    @Override
    public void injectArguments(String arguments) {
    }

    @Override
    public void execute(GameThread controller) throws InterruptedException {
        controller.setWatchUnready(true);
    }
}
