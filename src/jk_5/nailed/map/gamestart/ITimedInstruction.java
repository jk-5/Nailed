package jk_5.nailed.map.gamestart;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public interface ITimedInstruction extends IInstruction {

    public boolean shouldContinue(int ticks);
}
