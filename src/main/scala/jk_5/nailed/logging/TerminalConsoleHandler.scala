package jk_5.nailed.logging

import jline.console.ConsoleReader
import java.util.logging.{Level, Logger, ConsoleHandler}
import jk_5.nailed.Nailed
import java.io.IOException

/**
 * No description given
 *
 * @author jk-5
 */
class TerminalConsoleHandler(private final val reader: ConsoleReader) extends ConsoleHandler() {

  override def flush() = this.synchronized{
    try{
      if(Nailed.useJLine){
        this.reader.print(ConsoleReader.RESET_LINE + "")
        this.reader.flush()
        super.flush()
        try{
          this.reader.drawLine()
        }catch{
          case e: Throwable => this.reader.getCursorBuffer.clear()
        }
        this.reader.flush()
      }else super.flush()
    }catch{
      case e: IOException => Logger.getLogger(this.getClass.getName).log(Level.SEVERE, null, e)
    }
  }
}
