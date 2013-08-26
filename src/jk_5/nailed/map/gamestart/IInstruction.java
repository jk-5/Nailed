package jk_5.nailed.map.gamestart;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public interface IInstruction {

    public void injectArguments(String arguments);

    public void execute(GameStartupThread controller) throws Exception;
}
