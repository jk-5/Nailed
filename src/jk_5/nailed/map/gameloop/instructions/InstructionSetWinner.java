package jk_5.nailed.map.gameloop.instructions;

import jk_5.nailed.Nailed;
import jk_5.nailed.map.gameloop.GameThread;
import jk_5.nailed.map.gameloop.IInstruction;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class InstructionSetWinner implements IInstruction {

    private String winner;

    @Override
    public void injectArguments(String arguments) {
        this.winner = arguments;
    }

    @Override
    public void execute(GameThread controller) throws InterruptedException {
        controller.setWinner(Nailed.teamRegistry.getTeam(this.winner));
    }
}
