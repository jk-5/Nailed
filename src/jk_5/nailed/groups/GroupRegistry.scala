package jk_5.nailed.groups

import scala.collection.mutable

/**
 * No description given
 *
 * @author jk-5
 */
object GroupRegistry {

  private final val groups = mutable.HashMap[String, Group]()
  private var defaultGroup: Group = _

  @inline def registerGroup(name: String, group: Group) = this.groups.put(name, group)
  @inline def getGroup(name: String) = this.groups.get(name)
  @inline def getGroupID(group: Group) = this.groups.find(_._2 == group).get._1
  @inline def getDefaultGroup = this.defaultGroup
  @inline def setDefaultGroup(name: String) = this.getGroup(name).foreach(g => this.defaultGroup = g)
  @inline def getGroups = this.groups.keySet
}
