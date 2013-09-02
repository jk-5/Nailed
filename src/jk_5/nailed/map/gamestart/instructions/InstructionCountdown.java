package jk_5.nailed.map.gamestart.instructions;

import jk_5.nailed.Nailed;
import jk_5.nailed.map.gamestart.GameThread;
import jk_5.nailed.map.gamestart.ITimedInstruction;
import jk_5.nailed.players.Player;
import jk_5.nailed.util.EnumColor;
import jk_5.nailed.util.ServerUtils;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class InstructionCountdown implements ITimedInstruction {

    private int ticks;
    private String message;

    @Override
    public void injectArguments(String arguments) {
        String args[] = arguments.split(",");
        this.ticks = Integer.parseInt(args[0]);
        this.message = args[1];
    }

    @Override
    public void execute(GameThread controller) throws InterruptedException {

    }

    @Override
    public boolean shouldContinue(int ticks) {
        if ((this.ticks - ticks) == 60 || (this.ticks - ticks) == 30 || (this.ticks - ticks) == 20 || (this.ticks - ticks) == 10 || (this.ticks - ticks) <= 5 || ticks % 60 == 0) {
            ServerUtils.broadcastChatMessage(EnumColor.GREEN + "[Nail] " + EnumColor.RESET + message + EnumColor.GOLD + formatSeconds(this.ticks - ticks));
            if ((this.ticks - ticks) <= 5) {
                for (Player player : Nailed.playerRegistry.getPlayers()) {
                    player.playSound("note.harp", 1.5f, (this.ticks - ticks) == 0 ? 2 : 1);
                }
            }
        }
        return ticks >= this.ticks;
    }

    private static String formatSeconds(int seconds) {
        if (seconds < 60) return String.format("%d second%s", seconds, seconds == 1 ? "" : "s");
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        if (secs == 0) return String.format("%d minute%s", minutes, minutes == 1 ? "" : "s");
        else
            return String.format("%d minute%s and %d second%s", minutes, minutes == 1 ? "" : "s", seconds, seconds == 1 ? "" : "s");
    }
}
