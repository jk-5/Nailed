package jk_5.nailed.map.gamestart.instructions;

import jk_5.nailed.Nailed;
import jk_5.nailed.map.gamestart.GameThread;
import jk_5.nailed.map.gamestart.IInstruction;
import jk_5.nailed.players.Player;
import jk_5.nailed.teams.Team;
import net.minecraft.src.ChunkCoordinates;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class InstructionSetSpawnpoint implements IInstruction {

    private ChunkCoordinates coordinates;
    private String team;

    @Override
    public void injectArguments(String arguments) {
        String data[] = arguments.split(",", 4);
        this.coordinates = new ChunkCoordinates(Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
        this.team = data[0];
    }

    @Override
    public void execute(GameThread controller) throws InterruptedException {
        Team team = Nailed.teamRegistry.getTeam(this.team);
        for (Player p : Nailed.playerRegistry.getPlayers()) {
            if (p.getTeam() == team) {
                p.getEntity().setSpawnChunk(this.coordinates, true);
            }
        }
    }
}
