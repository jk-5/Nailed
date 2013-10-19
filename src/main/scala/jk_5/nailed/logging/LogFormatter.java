package jk_5.nailed.logging;

import jk_5.nailed.Nailed;
import jk_5.nailed.util.ChatColor;
import jline.Terminal;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Attribute;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final Terminal terminal;
    private final Map<ChatColor, String> replacements = new EnumMap<ChatColor, String>(ChatColor.class);
    private final ChatColor[] colors = ChatColor.values();

    public LogFormatter(){
        super();
        this.terminal = Nailed.reader().getTerminal();

        replacements.put(ChatColor.BLACK, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
        replacements.put(ChatColor.DARK_BLUE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
        replacements.put(ChatColor.DARK_GREEN, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
        replacements.put(ChatColor.DARK_AQUA, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
        replacements.put(ChatColor.DARK_RED, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
        replacements.put(ChatColor.DARK_PURPLE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
        replacements.put(ChatColor.GOLD, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
        replacements.put(ChatColor.GRAY, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
        replacements.put(ChatColor.DARK_GRAY, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString());
        replacements.put(ChatColor.BLUE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString());
        replacements.put(ChatColor.GREEN, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString());
        replacements.put(ChatColor.AQUA, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString());
        replacements.put(ChatColor.RED, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.RED).bold().toString());
        replacements.put(ChatColor.LIGHT_PURPLE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString());
        replacements.put(ChatColor.YELLOW, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString());
        replacements.put(ChatColor.WHITE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());
        replacements.put(ChatColor.MAGIC, Ansi.ansi().a(Attribute.BLINK_SLOW).toString());
        replacements.put(ChatColor.BOLD, Ansi.ansi().a(Attribute.UNDERLINE_DOUBLE).toString());
        replacements.put(ChatColor.STRIKETHROUGH, Ansi.ansi().a(Attribute.STRIKETHROUGH_ON).toString());
        replacements.put(ChatColor.UNDERLINE, Ansi.ansi().a(Attribute.UNDERLINE).toString());
        replacements.put(ChatColor.ITALIC, Ansi.ansi().a(Attribute.ITALIC).toString());
        replacements.put(ChatColor.RESET, Ansi.ansi().a(Attribute.RESET).toString());
    }

    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();

        builder.append(this.dateFormat.format(record.getMillis()));
        builder.append(" [");
        builder.append(record.getLoggerName());
        builder.append("] [");
        builder.append(record.getLevel().getName());
        builder.append("] ");
        builder.append(this.formatMessage(record));
        builder.append('\n');
        Throwable thrown = record.getThrown();

        if (thrown != null) {
            StringWriter writer = new StringWriter();
            thrown.printStackTrace(new PrintWriter(writer));
            builder.append(writer.toString());
        }

        return this.transformColors(builder.toString());
    }

    public String transformColors(String input){
        if (this.terminal.isAnsiSupported()) {
            for (ChatColor color : colors) {
                if (replacements.containsKey(color)) {
                    input = input.replaceAll("(?i)" + color.toString(), replacements.get(color));
                } else {
                    input = input.replaceAll("(?i)" + color.toString(), "");
                }
            }
            return input + Ansi.ansi().reset().toString();
        } else {
            return input;
        }
    }
}
