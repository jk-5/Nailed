package jk_5.nailed.command

import jk_5.nailed.players.PlayerRegistry
import jk_5.nailed.team.Team
import jk_5.nailed.util.EnumColor
import scala.collection.mutable

/**
 * No description given
 *
 * @author jk-5
 */
object CommandTeamleader extends TCommand {
  val commandName = "teamleader"
  this.permissionLevel = 2

  override def getCommandUsage = "/teamleader - Sets the teamleaders"
  def processCommand(sender: CommandSender, args: Array[String]){
    val currentMap = sender.map
    args.length match {
      case 0 => throw new WrongUsageException("/teamleader <set:unset> <player>")
      case 1 => if(args(0) == "unset" || args(0) == "set") throw new WrongUsageException("/teamleader <set:unset> <player>")
      case 2 => {
        if(args(0) == "set"){
          val player = PlayerRegistry.getPlayer(args(1))
          if(player.isEmpty) throw new CommandException("Player " + args(0) + " was not found")
          if(player.get.getTeam == Team.UNKNOWN) throw new CommandException("Player " + args(1) + " does not have a team")
          player.get.getTeam.setTeamLeader(player.get)
          currentMap.sendMessageToAllPlayersWithoutPrefix(player.get.getGroup.getChatPrefix + player.get.getChatFormattedName + EnumColor.GREEN + " is now the leader of team " + player.get.getTeam)
        }else if(args(0) == "unset"){
          val player = PlayerRegistry.getPlayer(args(1))
          if(player.isEmpty) throw new CommandException("Player " + args(0) + " was not found")
          if(player.get.getTeam.getTeamLeader == player.get) throw new CommandException("Player " + args(1) + " is not the teamleader of his team")
          player.get.getTeam.setTeamLeader(null)
          currentMap.sendMessageToAllPlayersWithoutPrefix(player.get.getGroup.getChatPrefix + player.get.getChatFormattedName + EnumColor.GOLD + " is no longer the leader of team " + player.get.getTeam)
        }
      }
      case _ => throw new WrongUsageException("/teamleader <set:unset> <player>")
    }
  }

  override def addAutocomplete(s: CommandSender, args: Array[String]): mutable.Buffer[String] = args.length match{
    case 1 => Command.getMatched(args, "set", "unset")
    case 2 => if(args(0) == "set" || args(0) == "unset"){
      val p = PlayerRegistry.getPlayer(args(1))
      if(p.isEmpty) return null
      Command.getMatched(args, p.get.getTeam.getAllPlayerNames)
    } else null
  }
}
