package jk_5.nailed.map.gameloop;

/**
 * No description given
 *
 * @author jk-5
 */
public interface IInstruction {

    public void injectArguments(String arguments);

    public void execute(GameThread controller) throws Exception;
}
