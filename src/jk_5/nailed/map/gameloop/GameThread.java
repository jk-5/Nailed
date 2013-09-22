package jk_5.nailed.map.gameloop;

import jk_5.nailed.Nailed;
import jk_5.nailed.players.Player;
import jk_5.nailed.teams.Team;
import jk_5.nailed.util.EnumColor;
import jk_5.nailed.util.ServerUtils;

import java.util.Iterator;
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
        if (this.instructions.size() == 0) return;
        this.gameRunning = true;
        IInstruction current = null;
        Iterator<IInstruction> iterator = this.instructions.iterator();
        try {
            while (this.gameRunning && this.winner == null && iterator.hasNext()) {
                current = iterator.next();
                if (current instanceof ITimedInstruction) {
                    int ticks = 0;
                    ITimedInstruction timer = (ITimedInstruction) current;
                    while (this.gameRunning && !timer.shouldContinue(this, ticks) && this.winner == null) {
                        ticks++;
                        Thread.sleep(1000);
                    }
                } else current.execute(this);
            }
        } catch (Exception e) {
            ServerUtils.broadcastChatMessage(EnumColor.RED.toString() + e.getClass().getName() + " was thrown in the game loop. Game cancelled!");
            if (current != null)
                ServerUtils.broadcastChatMessage(EnumColor.RED.toString() + "Current instruction: " + current.getClass().getName());
            else
                ServerUtils.broadcastChatMessage(EnumColor.RED.toString() + "Current instruction is null! (Wow, that\'s weired!)");
            System.err.println(e.getClass().getName() + " was thrown in the game loop. Game cancelled!");
            e.printStackTrace();
        }
        for (Player p : this.map.getPlayers()) {
            if (p.getEntity() != null) p.getEntity().setSpawnChunk(null, false);
            if (Nailed.teamspeak.isEnabled() && p.getTeamspeakClient() != null)
                Nailed.teamspeak.moveClientToChannel(p.getTeamspeakClient(), 14); //FIXME: lobby!
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
        if (this.winner != null && !this.interruptWin) return;
        this.winner = team;
        this.gameRunning = false;
        Nailed.statManager.enableStat("gamewon");
        this.getMap().sendMessageToAllPlayers(EnumColor.GOLD + "Winning team: " + winner.toString());
    }

    public void notifyReadyUpdate(){
        boolean allReady = true;
        for(Team team : this.map.getTeamManager().getTeams()){
            if(!team.isReady()) allReady = false;
        }
        if(!allReady && this.watchUnready && this.gameRunning){
            this.gameRunning = false;
            this.getMap().sendMessageToAllPlayers("The game was stopped because not all teams are ready");
        }else if(allReady && !this.gameRunning){
            this.start();
        }
    }
}
