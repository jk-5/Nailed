package jk_5.nailed.map.gamestart.instructions;

import jk_5.nailed.Nailed;
import jk_5.nailed.map.gamestart.GameThread;
import jk_5.nailed.map.gamestart.IInstruction;
import jk_5.nailed.teams.Team;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class InstructionSetWinner implements IInstruction {

    private Team winner;

    @Override
    public void injectArguments(String arguments) {
        this.winner = Nailed.teamRegistry.getTeam(arguments);
    }

    @Override
    public void execute(GameThread controller) throws InterruptedException {
        controller.setWinner(this.winner);
    }
}
