package jk_5.nailed.logging;

import net.minecraft.logging.ILogAgent;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * No description given
 *
 * @author jk-5
 */
public class VanillaLogAgent implements ILogAgent {

    private final Logger logger = Logger.getLogger("Minecraft");

    public Logger func_120013_a() {
        return this.logger;
    }

    public void logInfo(String par1Str) {
        this.logger.log(Level.INFO, par1Str);
    }

    public void logWarning(String par1Str) {
        this.logger.log(Level.WARNING, par1Str);
    }

    public void logWarningFormatted(String par1Str, Object ... par2ArrayOfObj) {
        this.logger.log(Level.WARNING, par1Str, par2ArrayOfObj);
    }

    public void logWarningException(String par1Str, Throwable par2Throwable) {
        this.logger.log(Level.WARNING, par1Str, par2Throwable);
    }

    public void logSevere(String par1Str) {
        this.logger.log(Level.SEVERE, par1Str);
    }

    public void logSevereException(String par1Str, Throwable par2Throwable) {
        this.logger.log(Level.SEVERE, par1Str, par2Throwable);
    }
}
