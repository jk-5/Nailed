package jk_5.nailed.logging

import java.util.logging.{LogManager, Level, Logger}
import java.io.PrintStream
import jk_5.nailed.Nailed

/**
 * No description given
 *
 * @author jk-5
 */
object NailedLogging {

  final val sysOut = System.out

  private final val globalLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
  private final val nailedLogger = Logger.getLogger("Nailed")
  private final val vanillaLogger = Logger.getLogger("Minecraft")
  private final val sysoutLogger = Logger.getLogger("STDOUT")
  private final val syserrLogger = Logger.getLogger("STDERR")

  private var consoleHandler: TerminalConsoleHandler = _

  def init(){
    this.consoleHandler = new TerminalConsoleHandler(Nailed.reader)
    LogManager.getLogManager.reset()

    this.globalLogger.setLevel(Level.ALL)
    this.globalLogger.getHandlers.foreach(h => globalLogger.removeHandler(h))
    this.globalLogger.addHandler(this.consoleHandler)
    this.consoleHandler.setFormatter(new LogFormatter)

    System.setOut(new PrintStream(new LoggerOutputStream(this.sysoutLogger, Level.INFO), true))
    System.setErr(new PrintStream(new LoggerOutputStream(this.syserrLogger, Level.SEVERE), true))

    this.nailedLogger.setParent(this.globalLogger)
    this.vanillaLogger.setParent(this.globalLogger)
    this.sysoutLogger.setParent(this.globalLogger)
    this.syserrLogger.setParent(this.globalLogger)

    this.nailedLogger.info("Initialized")
  }
}
