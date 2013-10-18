package jk_5.nailed.command

import jk_5.nailed.players.PlayerRegistry
import jk_5.nailed.util.EnumColor
import jk_5.nailed.team.Team

/**
 * No description given
 *
 * @author jk-5
 */
object CommandTeam extends TCommand{
  val commandName = "team"

  override def canCommandSenderUseCommand(sender: CommandSender) = sender.isValidPlayer && (sender.player.isAdmin || sender.player.getTeam.getTeamLeader == sender.player)
  override def getCommandUsage = "/team - Adds a player to a team"

  def processCommand(sender: CommandSender, args: Array[String]) = args.length match{
    case 0 => throw new WrongUsageException("/team <join:leave>")
    case 1 => if (args(0) == "join"){
        if(sender.player.isAdmin) throw new WrongUsageException("/team join <player> <" + sender.map.getTeamManager.getTeamNames.mkString(":") + ">")
        else throw new WrongUsageException("/team join <player>")
      }else if (args(0) == "leave") throw new WrongUsageException("/team leave <player>")
    case 2 => if(args(0) == "join"){
        val p = PlayerRegistry.getPlayer(args(1))
        if(p.isEmpty) throw new CommandException("Player " + args(1) + " was not found")
        else if(sender.player.isTeamLeader){
          if(p.get.isTeamLeader) throw new CommandException("Player " + p.get.getUsername + " is teamleader of another team")
          p.get.setTeam(sender.player.getTeam)
          p.get.sendChatMessage(EnumColor.GREEN + "You have been added to the team " + p.get.getTeam.getColor + p.get.getTeam.getName)
          sender.sendChatMessage(EnumColor.GREEN + "Player " + p.get.getUsername + " was added to the team " + p.get.getTeam.getColor + p.get.getTeam.getName)
        }else if(sender.player.isAdmin) throw new WrongUsageException("/team join <player> <" + sender.map.getTeamManager.getTeamNames.mkString(":") + ">")
      }else if(args(0) == "leave"){
        val p = PlayerRegistry.getPlayer(args(1))
        if(p.isEmpty) throw new CommandException("Player " + args(1) + " was not found")
        else if(sender.player.isTeamLeader || sender.player.isAdmin){
          if(sender.player.isTeamLeader && p.get.getTeam != sender.player.getTeam && !sender.player.isAdmin) throw new CommandException("Player " + p.get.getUsername + " is not in your team")
          p.get.sendChatMessage(EnumColor.GREEN + "You have been removed from the team " + p.get.getTeam.getColor + p.get.getTeam.getName)
          sender.sendChatMessage(EnumColor.GREEN + "Player " + p.get.getUsername + " was removed from the team " + p.get.getTeam.getColor + p.get.getTeam.getName)
          if(p.get.isTeamLeader){
            p.get.getTeam.setTeamLeader(null)
            sender.map.sendMessageToAllPlayersWithoutPrefix(p.get.getGroup.getChatPrefix + p.get.getChatFormattedName + EnumColor.GOLD + " is no longer the leader of team " + p.get.getTeam)
          }
          p.get.setTeam(Team.UNKNOWN)
        }
      }
    case 3 => if(args(0) == "join" && sender.player.isAdmin){
        val p = PlayerRegistry.getPlayer(args(1))
        val t = sender.map.getTeamManager.getTeam(args(2))
        if(p.isEmpty) throw new CommandException("Player " + args(1) + " was not found")
        if(t.isEmpty) throw new CommandException("Team " + args(2) + " was not found")
        if(p.get.getTeam != null && p.get.isTeamLeader){
          p.get.getTeam.setTeamLeader(null)
          sender.map.sendMessageToAllPlayersWithoutPrefix(p.get.getGroup.getChatPrefix + p.get.getChatFormattedName + EnumColor.GOLD + " is no longer the leader of team " + p.get.getTeam)
        }
        p.get.setTeam(t.get)
        p.get.sendChatMessage(EnumColor.GREEN + "You have been added to the team " + p.get.getTeam.getColor + p.get.getTeam.getName)
      }else if(sender.player.isTeamLeader) throw new WrongUsageException("/team <join:leave> <player>")
    case _ => throw new WrongUsageException("/team <join:leave>")
  }

  override def addAutocomplete(sender: CommandSender, args: Array[String]): AutocompleteList = args.length match{
    case 1 => Command.getMatched(args, "join", "leave")
    case 2 => if(args(0) == "join" || args(0) == "leave") Command.getMatched(args, Command.getAllUsernames) else null
    case 3 => if(args(0) == "join" && sender.player.isAdmin) Command.getMatched(args, sender.map.getTeamManager.getTeamNames) else null
    case _ => null
  }
}
