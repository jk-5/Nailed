package jk_5.nailed.groups

import jk_5.nailed.util.EnumColor

/**
 * No description given
 *
 * @author jk-5
 */
case class Group(private final val name: String, private final val nameColor: EnumColor) {
  @inline final def getGroupID = GroupRegistry.getGroupID(this)
  @inline def getNameColor = this.nameColor
  @inline def getChatPrefix = ""
  @inline def getName = this.name
}

class GroupPlayer extends Group("Player", EnumColor.GREY)
class GroupAdmin extends Group("Admin", EnumColor.GREY) {
  override def getChatPrefix = EnumColor.GREEN + "[Admin] " + EnumColor.RESET
}

