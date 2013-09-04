package jk_5.nailed.map.gameloop;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jk_5.nailed.Nailed;
import jk_5.nailed.map.gameloop.instructions.*;
import jk_5.nailed.players.Player;
import jk_5.nailed.teams.Team;
import jk_5.nailed.util.EnumColor;
import jk_5.nailed.util.ServerUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class GameThread extends Thread {

    private static final Map<String, Class<?>> instructionMap = Maps.newHashMap();

    private final List<IInstruction> instructions = Lists.newArrayList();

    private boolean gameRunning = false;
    private boolean watchUnready = false;
    private boolean interruptWin = false;
    private Team winner = null;

    static {
        instructionMap.put("trigger", InstructionTrigger.class);
        instructionMap.put("sleep", InstructionSleep.class);
        instructionMap.put("watchunready", InstructionWatchUnready.class);
        instructionMap.put("unwatchunready", InstructionUnwatchUnready.class);
        instructionMap.put("countdown", InstructionCountdown.class);
        instructionMap.put("setwinner", InstructionSetWinner.class);
        instructionMap.put("startwinnerinterrupt", InstructionStartWinnerInterrupt.class);
        instructionMap.put("enable", InstructionEnableStat.class);
        instructionMap.put("disable", InstructionDisableStat.class);
        instructionMap.put("setspawn", InstructionSetSpawnpoint.class);
        instructionMap.put("resetspawn", InstructionResetSpawnpoint.class);
        instructionMap.put("clearinventory", InstructionClearInventory.class);
        instructionMap.put("setgamemode", InstructionSetGamemode.class);
    }

    public GameThread() {
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
        this.gameRunning = false;
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

    private static IInstruction getInstruction(String name) {
        try {
            return (IInstruction) instructionMap.get(name).newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public void parseInstructions(ZipInputStream stream) {
        System.out.println("Parsing instructions file...");
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        this.instructions.clear();
        int lineNumber = 1;
        try {
            while (in.ready()) {
                String line = in.readLine();
                if (line.startsWith("#")) continue;
                String data[] = line.split(" ", 2);
                if (data.length == 0) continue;
                IInstruction instr = getInstruction(data[0].trim());
                if (instr == null) continue;
                if (data.length == 2) instr.injectArguments(data[1]);
                this.instructions.add(instr);
                lineNumber++;
            }
            if (this.instructions.isEmpty())
                throw new RuntimeException("None of the instructions in the file could be read");
        } catch (Exception e) {
            System.err.println("Error while parsing instructions file at line " + lineNumber + ":");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
