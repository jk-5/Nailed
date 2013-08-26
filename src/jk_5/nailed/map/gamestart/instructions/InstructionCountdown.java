package jk_5.nailed.map.gamestart.instructions;

import jk_5.nailed.map.gamestart.GameStartupThread;
import jk_5.nailed.map.gamestart.ITimedInstruction;
import jk_5.nailed.util.EnumColor;
import jk_5.nailed.util.ServerUtils;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class InstructionCountdown implements ITimedInstruction {

    private int ticks;
    private String message;

    @Override
    public void injectArguments(String arguments) {
        String args[] = arguments.split(",");
        this.ticks = Integer.parseInt(args[0]);
        this.message = args[1];
    }

    @Override
    public void execute(GameStartupThread controller) throws InterruptedException {

    }

    @Override
    public boolean shouldContinue(int ticks) {
        if (this.ticks <= 30) {
            ServerUtils.broadcastChatMessage(EnumColor.GREEN + "[Nail] " + EnumColor.RESET + message + (this.ticks - ticks));
        } else {
            if (ticks % 60 == 0) {
                ServerUtils.broadcastChatMessage(EnumColor.GREEN + "[Nail] " + EnumColor.RESET + message + (this.ticks - ticks));
            }
        }
        return ticks >= this.ticks;
    }
}
