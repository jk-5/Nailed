package jk_5.nailed.map.gamestart;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jk_5.nailed.Nailed;
import jk_5.nailed.map.gamestart.instructions.*;
import jk_5.nailed.players.Player;
import jk_5.nailed.teams.Team;
import jk_5.nailed.util.EnumColor;
import jk_5.nailed.util.ServerUtils;

import java.util.List;
import java.util.Map;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class GameThread extends Thread {

    private static final Map<String, Class<?>> instructionMap = Maps.newHashMap();
    private final List<IInstruction> instructions;

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
        String instructionList = Nailed.mapManager.getConfig().getProperty("gamescript", "");
        this.instructions = parseInstructions(instructionList);
        this.setDaemon(true);
        this.setName("GameStartup");
    }

    @Override
    public void run() {
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
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            ServerUtils.broadcastChatMessage(EnumColor.RED.toString() + e.getClass().getName() + " was thrown in the game startup loop. Game cancelled!");
            ServerUtils.broadcastChatMessage(EnumColor.RED.toString() + "Current instruction: " + current.getClass().getName());
            System.err.println(e.getClass().getName() + " was thrown in the game startup loop. Game cancelled!");
            e.printStackTrace();
        }
        for (Player p : Nailed.playerRegistry.getPlayers()) {
            if (p.getEntity() != null) p.getEntity().setSpawnChunk(null, false);
        }
    }

    public void setWatchUnready(boolean watch) {
        this.watchUnready = watch;
    }

    public void setWinnerInterrupt(boolean interrupt) {
        this.interruptWin = interrupt;
    }

    public void setWinner(Team team) {
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

    public static List<IInstruction> parseInstructions(String instructionList) {
        System.out.println(instructionList);
        List<IInstruction> instr = Lists.newArrayList();
        String instructions[] = instructionList.split(";");
        for (String instruction : instructions) {
            String arg[] = instruction.split("\\(", 2);
            IInstruction instruct = getInstruction(arg[0]);
            instruct.injectArguments(arg[1].substring(0, arg[1].length() - 1));
            instr.add(instruct);
        }
        return instr;
    }
}
