package jk_5.nailed.map.gameloop;

/**
 * No description given
 *
 * @author jk-5
 */
public interface ITimedInstruction extends IInstruction {

    public boolean shouldContinue(GameThread controller, int ticks);
}
