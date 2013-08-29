package jk_5.nailed.map.gamestart.instructions;

import jk_5.nailed.Nailed;
import jk_5.nailed.map.gamestart.GameThread;
import jk_5.nailed.map.gamestart.IInstruction;
import jk_5.nailed.players.Player;
import jk_5.nailed.teams.Team;

/**
 * TODO: Edit description
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
        Team team = Nailed.teamRegistry.getTeam(this.team);
        for (Player p : Nailed.playerRegistry.getPlayers()) {
            if (p.getEntity() == null) continue;
            if (p.getTeam() == team) {
                p.getEntity().setSpawnChunk(null, false);
            }
        }
    }
}
