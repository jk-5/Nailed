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
public class InstructionSetFoodLevel implements IInstruction {

    private String team;
    private int foodLevel;

    @Override
    public void injectArguments(String arguments) {
        String data[] = arguments.split(" ", 2);
        this.team = data[0];
        this.foodLevel = Integer.parseInt(data[1]);
    }

    @Override
    public void execute(GameThread controller) throws InterruptedException {
        Team team = controller.getMap().getTeamManager().getTeam(this.team);
        for (Player p : Nailed.playerRegistry.getPlayers()) {
            if (p.getEntity() == null) continue;
            if (p.getTeam() == team) {
                p.getEntity().getFoodStats().addStats(this.foodLevel - p.getEntity().getFoodStats().getFoodLevel(), 0);
            }
        }
    }
}
