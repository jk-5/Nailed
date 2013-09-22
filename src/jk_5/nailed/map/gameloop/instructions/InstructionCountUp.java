package jk_5.nailed.map.gameloop.instructions;

import jk_5.nailed.Nailed;
import jk_5.nailed.map.gameloop.GameThread;
import jk_5.nailed.map.gameloop.ITimedInstruction;
import jk_5.nailed.players.Player;

/**
 * No description given
 *
 * @author jk-5
 */
public class InstructionCountUp implements ITimedInstruction {

    private String message;

    @Override
    public void injectArguments(String arguments) {
        this.message = arguments;
    }

    @Override
    public void execute(GameThread controller) {
    }

    @Override
    public boolean shouldContinue(GameThread controller, int ticks) {
        if (ticks % 60 == 0) {
            controller.getMap().sendMessageToAllPlayers(String.format(message, formatSeconds(ticks)));
        }
        return true;
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
