package jk_5.nailed.logging

import java.io.ByteArrayOutputStream
import java.util.logging.{Level, Logger}

/**
 * No description given
 *
 * @author jk-5
 */
class LoggerOutputStream(private final val logger: Logger, private final val level: Level) extends ByteArrayOutputStream() {
  private final val separator = sys.props.get("line.separator").getOrElse("\n")

  override def flush() = this.synchronized{
    super.flush()
    val record = this.toString
    super.reset()
    if(!record.isEmpty && !record.equals(this.separator)){
      this.logger.logp(this.level, "", "", record)
    }
  }
}
