package jk_5.nailed.command

import jk_5.nailed.Nailed
import scala.collection.JavaConversions

/**
 * No description given
 *
 * @author jk-5
 */
object CommandTeamspeak extends TCommand {
  val commandName = "teamspeak"
  this.addAlias("ts")

  @inline override def canCommandSenderUseCommand(sender: CommandSender) = sender.isValidPlayer
  @inline override def getCommandUsage = "/teamspeak - Teamspeak stuff"

  def processCommand(sender: CommandSender, args: Array[String]) = args.length match{
    case 0 => throw new WrongUsageException("/teamspeak <setname> <username>")
    case 1 => if(args(0).equals("setname")) throw new WrongUsageException("/teamspeak <setname> <username>")
    case 2 => if(args(0).equals("setname")){
      if(sender.isInvalidPlayer) throw new CommandException("You are nobody!")
      val ts = Nailed.teamspeak.getClientForUser(args(1))
      if(ts == null) throw new CommandException("The name you entered is not online on teamspeak")
      sender.player.setTeamspeakClient(ts)
    }
    case _ => throw new WrongUsageException("/teamspeak <setname> <username>")
  }

  override def addAutocomplete(sender: CommandSender, args: Array[String]): AutocompleteList = args.length match {
    case 1 => Command.getMatched(args, "setname")
    case 2 => if(args(0).equals("setname")) Command.getMatched(args, JavaConversions.collectionAsScalaIterable(Nailed.teamspeak.getNicknames)) else null
    case _ => null
  }
}
