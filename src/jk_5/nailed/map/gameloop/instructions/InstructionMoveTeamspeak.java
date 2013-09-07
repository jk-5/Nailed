package jk_5.nailed.map.gameloop.instructions;

import jk_5.nailed.Nailed;
import jk_5.nailed.map.gameloop.GameThread;
import jk_5.nailed.map.gameloop.IInstruction;
import jk_5.nailed.players.Player;
import jk_5.nailed.teams.Team;
import jk_5.nailed.teamspeak3.TeamspeakClient;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class InstructionMoveTeamspeak implements IInstruction {

    private String team;
    private int channelID;

    @Override
    public void injectArguments(String arguments) {
        String data[] = arguments.split(" ", 2);
        this.team = data[0];
        this.channelID = Integer.parseInt(data[1]);
    }

    @Override
    public void execute(GameThread controller) throws InterruptedException {
        Team team = Nailed.teamRegistry.getTeam(this.team);
        for (Player p : Nailed.playerRegistry.getPlayers()) {
            if(p.getTeam() != team) return;
            TeamspeakClient ts = p.getTeamspeakClient();
            if(ts == null) continue;
            Nailed.teamspeak.moveClientToChannel(ts, this.channelID);
        }
    }
}
