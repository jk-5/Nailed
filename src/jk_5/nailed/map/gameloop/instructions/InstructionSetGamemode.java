package jk_5.nailed.map.gameloop.instructions;

import jk_5.nailed.Nailed;
import jk_5.nailed.map.gameloop.GameThread;
import jk_5.nailed.map.gameloop.IInstruction;
import jk_5.nailed.players.Player;
import jk_5.nailed.teams.Team;
import net.minecraft.src.EnumGameType;

/**
 * No description given
 *
 * @author jk-5
 */
public class InstructionSetGamemode implements IInstruction {

    private String team;
    private int gamemode;

    @Override
    public void injectArguments(String arguments) {
        String data[] = arguments.split(" ", 2);
        this.team = data[0];
        this.gamemode = Integer.parseInt(data[1]);
    }

    @Override
    public void execute(GameThread controller) throws InterruptedException {
        Team team = controller.getMap().getTeamManager().getTeam(this.team);
        for (Player p : Nailed.playerRegistry.getPlayers()) {
            if (p.getEntity() == null) continue;
            if (p.getTeam() == team) {
                p.getEntity().setGameType(EnumGameType.getByID(this.gamemode));
            }
        }
    }
}
