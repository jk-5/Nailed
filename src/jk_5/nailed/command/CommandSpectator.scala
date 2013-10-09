package jk_5.nailed.command

/**
 * No description given
 *
 * @author jk-5
 */
object CommandSpectator extends TCommand {
  val commandName = "spectator"

  override def canCommandSenderUseCommand(sender: CommandSender) = sender.isValidPlayer
  override def getCommandUsage = "/spectator - Toggles spectator mode"
  def processCommand(sender: CommandSender, args: Array[String]){
    if(sender.isInvalidPlayer) throw new CommandException("I seriously don\'t know who you are!")
    if(!sender.map.getGameThread.isGameRunning) throw new CommandException("You can\'t join spectator mode when no game is running")
    sender.player.setSpectator(sender.player.isSpectator)
  }
}
