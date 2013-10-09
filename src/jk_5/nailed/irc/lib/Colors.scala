package jk_5.nailed.irc.lib

object Colors {

  def removeColors(line: String): String = {
    val length = line.length
    val buffer = new StringBuilder()
    var i = 0
    while (i < length) {
      var ch = line.charAt(i)
      if (ch == '\u0003') {
        i += 1
        if (i < length) {
          ch = line.charAt(i)
          if (Character.isDigit(ch)) {
            i += 1
            if (i < length) {
              ch = line.charAt(i)
              if (Character.isDigit(ch)) {
                i += 1
              }
            }
            if (i < length) {
              ch = line.charAt(i)
              if (ch == ',') {
                i += 1
                if (i < length) {
                  ch = line.charAt(i)
                  if (Character.isDigit(ch)) {
                    i += 1
                    if (i < length) {
                      ch = line.charAt(i)
                      if (Character.isDigit(ch)) {
                        i += 1
                      }
                    }
                  }else i -= 1
                }else i -= 1
              }
            }
          }
        }
      }else if (ch == '\u000f') i += 1
      else {
        buffer.append(ch)
        i += 1
      }
    }
    buffer.toString()
  }

  def removeFormatting(line: String): String = {
    val length = line.length
    val buffer = new StringBuilder()
    var i = 0
    while (i < length) {
      val ch: Char = line.charAt(i)
      if (!(ch == '\u000f' || ch == '\u0002' || ch == '\u001f' || ch == '\u0016')) buffer.append(ch)
      i += 1
    }
    buffer.toString()
  }

  def removeFormattingAndColors(line: String) = removeFormatting(removeColors(line))

  final val NORMAL: String = "\u000f"     //Reset
  final val BOLD: String = "\u0002"       //Bold
  final val UNDERLINE: String = "\u001f"  //Underline
  final val REVERSE: String = "\u0016"    //Italic
  final val WHITE: String = "\u000300"
  final val BLACK: String = "\u000301"
  final val DARK_BLUE: String = "\u000302"
  final val DARK_GREEN: String = "\u000303"
  final val RED: String = "\u000304"
  final val BROWN: String = "\u000305"
  final val PURPLE: String = "\u000306"
  final val OLIVE: String = "\u000307"
  final val YELLOW: String = "\u000308"
  final val GREEN: String = "\u000309"
  final val TEAL: String = "\u000310"
  final val CYAN: String = "\u000311"
  final val BLUE: String = "\u000312"
  final val MAGENTA: String = "\u000313"
  final val DARK_GRAY: String = "\u000314"
  final val LIGHT_GRAY: String = "\u000315"
}