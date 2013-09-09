package jk_5.nailed.map.gameloop.instructions;

import jk_5.nailed.map.gameloop.GameThread;
import jk_5.nailed.map.gameloop.IInstruction;

/**
 * No description given
 *
 * @author jk-5
 */
public class InstructionSetTime implements IInstruction {

    private int time;

    @Override
    public void injectArguments(String arguments) {
        this.time = Integer.parseInt(arguments);
    }

    @Override
    public void execute(GameThread controller) throws InterruptedException {
        controller.getMap().getWorld().setWorldTime(time % 24000);
    }
}
