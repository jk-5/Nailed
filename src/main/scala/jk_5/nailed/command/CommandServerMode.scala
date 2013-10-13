package jk_5.nailed.command

import jk_5.nailed.NailedEventFactory

/**
 * No description given
 *
 * @author jk-5
 */
object CommandServerMode extends TCommand{
  val commandName = "mode"

  override def canCommandSenderUseCommand(sender: CommandSender) = (sender.isValidPlayer && sender.player.isAdmin) || sender.isConsole
  override def getCommandUsage = "/mode <play:build:dev> - Changes the server mode"

  def processCommand(sender: CommandSender, args: Array[String]){
    if(args.length != 1) throw new WrongUsageException("/mode <play:build:dev>")
    if(args(0) == "play" || args(0) == "build" || args(0) == "dev") NailedEventFactory.mode == args(0)
    else throw new WrongUsageException("/mode <play:build:dev>")
  }
  override def addAutocomplete(sender: CommandSender, args: Array[String]): AutocompleteList = {
    if(args.length == 0) Command.getMatched(args, "build", "dev", "play") else null
  }
}
