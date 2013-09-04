package jk_5.nailed.map.gameloop.instructions;

import jk_5.nailed.Nailed;
import jk_5.nailed.map.gameloop.GameThread;
import jk_5.nailed.map.gameloop.ITimedInstruction;
import jk_5.nailed.players.Player;
import jk_5.nailed.util.EnumColor;
import jk_5.nailed.util.ServerUtils;

/**
 * Counts down a specific amount of time with the given message
 *
 * @author jk-5
 */
public class InstructionCountdown implements ITimedInstruction {

    private int ticks;
    private String message;

    @Override
    public void injectArguments(String arguments) {
        String args[] = arguments.split(" ", 2);
        this.message = args[1];
        String timedata = args[0].toLowerCase().trim();
        if (timedata.endsWith("sec")) this.ticks = Integer.parseInt(timedata.substring(0, timedata.length() - 3));
        else if (timedata.endsWith("min"))
            this.ticks = Integer.parseInt(timedata.substring(0, timedata.length() - 3)) * 60;
        else if (timedata.endsWith("hour"))
            this.ticks = Integer.parseInt(timedata.substring(0, timedata.length() - 4)) * 3600;
        else throw new RuntimeException("Unable to parse the countdown command properly");
    }

    @Override
    public void execute(GameThread controller) {
    }

    @Override
    public boolean shouldContinue(int ticks) {
        if ((this.ticks - ticks) == 60 || (this.ticks - ticks) == 30 || (this.ticks - ticks) == 20 || (this.ticks - ticks) == 10 || (this.ticks - ticks) <= 5 || ticks % 60 == 0) {
            ServerUtils.broadcastChatMessage(EnumColor.GREEN + "[Nail] " + EnumColor.RESET + String.format(message, formatSeconds(this.ticks - ticks)));
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
        StringBuilder builder = new StringBuilder();
        boolean hasText = false;
        if (hours > 0) {
            builder.append(hours);
            builder.append(" hour");
            if (hours > 1) builder.append("s");
            hasText = true;
        }
        if (minutes > 0) {
            if (hasText) if (secs > 0) builder.append(", ");
            else builder.append(" and ");
            builder.append(minutes);
            builder.append(" minute");
            if (minutes > 1) builder.append("s");
            hasText = true;
        }
        if (secs > 0) {
            if (hasText) builder.append(" and ");
            builder.append(secs);
            builder.append(" second");
            if (secs > 1) builder.append("s");
            hasText = true;
        }
        if (!hasText) builder.append("0 seconds");
        return builder.toString();
    }
}
