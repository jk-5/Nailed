package jk_5.nailed.util

import java.util
import jline.console.completer.Completer
import jk_5.nailed.Nailed
import scala.collection.JavaConversions._

/**
 * No description given
 *
 * @author jk-5
 */
object JLineAutoCompleter extends Completer {

  def complete(buffer: String, cursor: Int, candidates: util.List[CharSequence]): Int = {
    var completing = this.strip(buffer.substring(0, cursor))
    val words = completing.split(" ")
    val lastWord = words(words.length - 1)
    Nailed.server.getPossibleCompletions(Nailed.server, "/" + completing).asInstanceOf[util.List[String]].foreach(c => candidates.add(this.strip(c)))
    cursor - lastWord.length
  }

  def strip(input: String): String = {
    var in = input
    while(in.startsWith("/")) in = in.substring(1)
    in
  }
}
