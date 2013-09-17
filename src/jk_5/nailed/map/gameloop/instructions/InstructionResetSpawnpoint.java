package jk_5.nailed.map.gameloop.instructions;

import jk_5.nailed.Nailed;
import jk_5.nailed.map.gameloop.GameThread;
import jk_5.nailed.map.gameloop.IInstruction;
import jk_5.nailed.players.Player;
import jk_5.nailed.teams.Team;

/**
 * No description given
 *
 * @author jk-5
 */
public class InstructionResetSpawnpoint implements IInstruction {

    private String team;

    @Override
    public void injectArguments(String arguments) {
        this.team = arguments;
    }

    @Override
    public void execute(GameThread controller) throws InterruptedException {
        Team team = controller.getMap().getTeamManager().getTeam(this.team);
        team.setSpawnpoint(null);
    }
}
