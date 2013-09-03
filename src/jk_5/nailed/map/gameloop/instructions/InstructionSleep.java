package jk_5.nailed.map.gameloop.instructions;

import jk_5.nailed.map.gameloop.GameThread;
import jk_5.nailed.map.gameloop.IInstruction;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class InstructionSleep implements IInstruction {

    private long delay;

    @Override
    public void injectArguments(String arguments) {
        this.delay = Long.parseLong(arguments);
    }

    @Override
    public void execute(GameThread controller) throws InterruptedException {
        Thread.sleep(this.delay);
    }
}
