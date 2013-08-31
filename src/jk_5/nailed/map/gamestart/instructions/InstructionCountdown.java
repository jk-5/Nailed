package jk_5.nailed.map.gamestart.instructions;

import jk_5.nailed.Nailed;
import jk_5.nailed.map.gamestart.GameThread;
import jk_5.nailed.map.gamestart.ITimedInstruction;
import jk_5.nailed.players.Player;
import jk_5.nailed.util.EnumColor;
import jk_5.nailed.util.ServerUtils;

import java.text.DecimalFormat;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class InstructionCountdown implements ITimedInstruction {

    private int ticks;
    private String message;

    private static DecimalFormat format = new DecimalFormat("00");

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
        if ((this.ticks - ticks) <= 30) {
            ServerUtils.broadcastChatMessage(EnumColor.GREEN + "[Nail] " + EnumColor.RESET + message + (this.ticks - ticks));
            for (Player player : Nailed.playerRegistry.getPlayers()) {
                player.playSound("note.harp", 1.5f, 1);
            }
        } else {
            if (ticks % 60 == 0) {
                ServerUtils.broadcastChatMessage(EnumColor.GREEN + "[Nail] " + EnumColor.RESET + message + formatSeconds(this.ticks - ticks));
            }
        }
        return ticks >= this.ticks;
    }

    private static String formatSeconds(int seconds) {
        if (seconds < 60) return String.format("%d seconds");
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        if (hours == 0) return String.format("%s:%s", format.format(minutes), format.format(secs));
        else return String.format("%s:%s:%s", format.format(hours), format.format(minutes), format.format(secs));
    }
}
