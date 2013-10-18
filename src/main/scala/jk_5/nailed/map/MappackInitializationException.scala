package jk_5.nailed.map

/**
 * No description given
 *
 * @author jk-5
 */
class MappackInitializationException(msg: String, cause: Throwable = null) extends RuntimeException(msg, cause) {
  def this(pack: Mappack) = this("Error while reading mappack " + pack.internalName)
  def this(pack: Mappack, t: Throwable)  = this("Error while reading mappack " + pack.internalName, t)
  def this(pack: Mappack, msg: String) = this("Error while reading mappack " + pack.internalName + ": " + msg)
  def this(pack: Mappack, msg: String, t: Throwable) = this("Error while reading mappack " + pack.internalName + ": " + msg, t)
}