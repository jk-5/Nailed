package jk_5.nailed.map.gamestart.instructions;

import jk_5.nailed.map.gamestart.GameStartupThread;
import jk_5.nailed.map.gamestart.IInstruction;

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
    public void execute(GameStartupThread controller) throws InterruptedException {
        System.out.println("sleeping");
        Thread.sleep(this.delay);
    }
}
