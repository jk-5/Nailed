package jk_5.nailed.command

import scala.collection.mutable.ArrayBuffer
import scala.collection.{mutable, JavaConversions}
import java.util
import jk_5.nailed.players.PlayerRegistry
import net.minecraft.server.MinecraftServer
import jk_5.nailed.map.MapLoader
import net.minecraft.command.ICommandSender
import net.minecraft.tileentity.TileEntityCommandBlock
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.ChatMessageComponent

/**
 * No description given
 *
 * @author jk-5
 */
object Command{
  def getMatched(args: Array[String], possible: String*): mutable.Buffer[String] = {
    val lastWord = args.last
    val ret = mutable.ArrayBuffer[String]()
    possible.filter(_.toLowerCase.startsWith(lastWord.toLowerCase)).foreach(s => ret += s)
    ret
  }

  def getMatched(args: Array[String], possible: Iterable[String]): mutable.Buffer[String] = {
    val lastWord = args.last
    val ret = mutable.ArrayBuffer[String]()
    possible.filter(_.toLowerCase.startsWith(lastWord.toLowerCase)).foreach(s => ret += s)
    ret
  }

  @inline def getAllUsernames = MinecraftServer.getServer.getAllUsernames
}

trait TCommand extends CommandProxy {
  type WrongUsageException = net.minecraft.command.WrongUsageException
  type CommandException = net.minecraft.command.CommandException
  type CommandSender = jk_5.nailed.command.CommandSender
  type AutocompleteList = scala.collection.mutable.Buffer[String]

  protected val commandName: String
  protected var permissionLevel = 0
  private val aliases = ArrayBuffer[String]()

  def addAlias(alias: String) = this.aliases += alias

  final def getCommandUsage(sender: ICommandSender): String = this.getCommandUsage(new CommandSender(sender))
  final def getCommandName = this.commandName
  final def getCommandAliases = JavaConversions.bufferAsJavaList(this.aliases)
  final def addTabCompletionOptions(sender: ICommandSender, args: Array[String]): util.List[_] = {
    if(!this.canCommandSenderUseCommand(sender)) return new util.ArrayList[String](0)
    val res = this.addAutocomplete(new CommandSender(sender), args)
    if(res == null) new util.ArrayList[String](0) else JavaConversions.bufferAsJavaList(res)
  }
  final def canCommandSenderUseCommand(sender: ICommandSender) = this.canCommandSenderUseCommand(new CommandSender(sender))
  final def processCommand(sender: ICommandSender, args: Array[String]) = if(this.canCommandSenderUseCommand(sender)) this.processCommand(new CommandSender(sender), args)

  def getCommandUsage: String = "/" + this.commandName
  def getCommandUsage(sender: CommandSender): String = this.getCommandUsage
  def isUsernameIndex(args: Array[String], index: Int) = false
  def canCommandSenderUseCommand(sender: CommandSender) = sender.toCommandSender.canCommandSenderUseCommand(this.permissionLevel, this.commandName)
  def addAutocomplete(sender: CommandSender, args: Array[String]): mutable.Buffer[String] = mutable.Buffer[String]()
  def processCommand(sender: CommandSender, args: Array[String])
}

class CommandSender(private final val sender: ICommandSender){
  private final val worldMap = MapLoader.getMap(this.sender)

  @inline implicit def toCommandSender: ICommandSender = this.sender

  @inline def isCommandBlock = this.sender.isInstanceOf[TileEntityCommandBlock]
  @inline def isPlayer = this.sender.isInstanceOf[EntityPlayerMP]
  @inline def isConsole = this.sender.isInstanceOf[MinecraftServer]
  @inline def isValidPlayer = this.sender.isInstanceOf[EntityPlayerMP] && PlayerRegistry.getPlayer(this.sender).isDefined
  @inline def isInvalidPlayer = !this.isValidPlayer
  @inline def player = PlayerRegistry.getPlayer(this.sender).get
  @inline def map = this.worldMap
  @inline def sendChatMessage(msg: String) = this.sender.sendChatToPlayer(ChatMessageComponent.createFromText(msg))
}
