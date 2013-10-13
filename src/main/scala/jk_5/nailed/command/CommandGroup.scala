package jk_5.nailed.command

import jk_5.nailed.players.PlayerRegistry
import jk_5.nailed.groups.GroupRegistry
import jk_5.nailed.util.EnumColor

/**
 * No description given
 *
 * @author jk-5
 */
object CommandGroup extends TCommand {
  val commandName = "group"
  this.permissionLevel = 2

  override def getCommandUsage = "/group - Sets the group of a player"
  def processCommand(sender: CommandSender, args: Array[String]) = args.length match{
    case 0 => throw new WrongUsageException("/group <set>")
    case 1 => if(args(0) == "set") throw new WrongUsageException("/group set <player> <admin:player>")
    case 2 => if(args(0) == "set"){
      val p = PlayerRegistry.getPlayer(args(1))
      if(p.isEmpty) throw new CommandException("Player " + args(1) + " was not found")
      else throw new WrongUsageException("/group set <player> <admin:player>")
    }
    case 3 => if(args(0) == "set"){
      val p = PlayerRegistry.getPlayer(args(1))
      val g = GroupRegistry.getGroup(args(2))
      if(p.isEmpty) throw new CommandException("Player " + args(1) + " was not found")
      if(g.isEmpty) throw new CommandException("Group " + args(2) + " was not found")
      p.get.setGroup(g.get)
      p.get.sendChatMessage(EnumColor.GREEN + "You were moved to the group " + g.get.getName)
      sender.sendChatMessage(EnumColor.GREEN + "Moved player " + p.get.getUsername + " to group " + g.get.getName)
    }
    case _ => throw new WrongUsageException("/group <set>")
  }
  override def addAutocomplete(sender: CommandSender, args: Array[String]): AutocompleteList = args.length match{
    case 1 => Command.getMatched(args, "set")
    case 2 => if(args(0) == "set") Command.getMatched(args, Command.getAllUsernames) else null
    case 3 => if(args(0) == "set") Command.getMatched(args, GroupRegistry.getGroups) else null
    case _ => null
  }
}
