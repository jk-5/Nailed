package jk_5.nailed.map.gameloop;

import jk_5.nailed.Nailed;
import jk_5.nailed.players.Player;
import jk_5.nailed.teams.Team;
import jk_5.nailed.util.EnumColor;
import jk_5.nailed.util.ServerUtils;

import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public class GameThread extends Thread {

    private boolean gameRunning = false;
    private boolean watchUnready = false;
    private boolean interruptWin = false;
    private Team winner = null;

    private final jk_5.nailed.map.Map map;

    private final List<IInstruction> instructions;

    public GameThread(jk_5.nailed.map.Map map) {
        this.map = map;
        this.instructions = map.getMappack().getInstructions();
        this.setDaemon(true);
        this.setName("GameStartup");
    }

    @Override
    public void run() {
        this.gameRunning = true;
        IInstruction current = null;
        int ticks = 0;
        int instructionIndex = 0;
        try {
            while (this.winner == null) {
                if (current == null) current = this.instructions.get(instructionIndex);
                if (current instanceof ITimedInstruction) {
                    if (((ITimedInstruction) current).shouldContinue(ticks)) {
                        current = this.instructions.get(++instructionIndex);
                        if (this.winner != null) return;
                        ticks = -1;
                    }
                } else {
                    current.execute(this);
                    if (this.winner != null) return;
                    current = this.instructions.get(++instructionIndex);
                    ticks = -1;
                }
                ticks++;
                if (current instanceof ITimedInstruction) Thread.sleep(1000);
            }
        } catch (Exception e) {
            ServerUtils.broadcastChatMessage(EnumColor.RED.toString() + e.getClass().getName() + " was thrown in the game startup loop. Game cancelled!");
            if (current != null)
                ServerUtils.broadcastChatMessage(EnumColor.RED.toString() + "Current instruction: " + current.getClass().getName());
            else
                ServerUtils.broadcastChatMessage(EnumColor.RED.toString() + "Current instruction is null! (Wow, that\'s weired!)");
            System.err.println(e.getClass().getName() + " was thrown in the game startup loop. Game cancelled!");
            e.printStackTrace();
        }
        for (Player p : Nailed.playerRegistry.getPlayers()) {
            if (p.getEntity() != null) p.getEntity().setSpawnChunk(null, false);
        }
        for (Player p : Nailed.playerRegistry.getPlayers()) {
            if (p.getTeamspeakClient() == null) continue;
            Nailed.teamspeak.moveClientToChannel(p.getTeamspeakClient(), 14); //FIXME! lobby!
        }
        this.gameRunning = false;
    }

    public jk_5.nailed.map.Map getMap() {
        return this.map;
    }

    public void setWatchUnready(boolean watch) {
        this.watchUnready = watch;
    }

    public void setWinnerInterrupt(boolean interrupt) {
        this.interruptWin = interrupt;
    }

    public boolean isGameRunning() {
        return this.gameRunning;
    }

    public void setWinner(Team team) {
        if (this.winner != null) return;
        if (!this.interruptWin) return;
        this.winner = team;
        Nailed.statManager.enableStat("gamewon");
        ServerUtils.broadcastChatMessage(EnumColor.GREEN + "[Nail] " + EnumColor.GOLD + " Winning team: " + winner.toString());
    }
}
