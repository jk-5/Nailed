package jk_5.nailed.command

/**
 * No description given
 *
 * @author jk-5
 */
object CommandCB extends TCommand {
  val commandName = "cb"
  override val permissionLevel = 2

  @inline override def getCommandUsage = "/cb - Command for command block interaction with Nailed"
  @inline def processCommand(sender: CommandSender, args: Array[String]){
    if(sender.isCommandBlock){
      args(0) match{
        case "startgame" => sender.map.getGameThread.start()
        case "setwinner" => {
          if(sender.map.getTeamManager.getTeam(args(1)).isEmpty) throw new CommandException("The team you entered does not exist!")
          sender.map.getGameThread.setWinner(sender.map.getTeamManager.getTeam(args(1)).get)
        }
      }
    }else throw new CommandException("This command can only be used by command blocks")
  }
}
