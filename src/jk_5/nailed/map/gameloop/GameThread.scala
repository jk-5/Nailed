package jk_5.nailed.map.gameloop

import jk_5.nailed.team.Team
import jk_5.nailed.map.Map
import jk_5.nailed.util.EnumColor
import jk_5.nailed.map.stats.StatManager
import scala.collection.mutable
import jk_5.nailed.teamspeak3.TeamspeakManager

/**
 * No description given
 *
 * @author jk-5
 */
class GameThread(private final val map: Map) extends Thread {
  this.setDaemon(true)
  this.setName("Gameloop-" + map.getFolderName)

  private var gameRunning = false
  private var watchUnready = false
  private var interruptWin = false
  private var winner: Team = null
  private final val instructions: mutable.Buffer[IInstruction] = this.map.getMappack.instructions.toBuffer

  override def run(){
    if(this.instructions.size == 0) return
    this.gameRunning = true
    var current: IInstruction = null
    val iterator = this.instructions.iterator
    try{
      while(this.gameRunning && this.winner == null && iterator.hasNext){
        current = iterator.next()
        current match {
          case timer: TTimedInstruction => {
            var ticks = 0
            while(this.gameRunning && !timer.shouldContinue(this, ticks) && this.winner == null){
              ticks += 1
              Thread.sleep(1000)
            }
          }
          case c: IInstruction => c.execute(this)
        }
      }
    }catch{
      case e: Exception => {
        this.map.sendMessageToAllPlayers(EnumColor.RED + e.getClass.getName + " was thrown in the game loop. Game stopped!")
        if(current != null) this.map.sendMessageToAllPlayers(EnumColor.RED + "Current instruction: " + current.getClass.getName)
        else this.map.sendMessageToAllPlayers(EnumColor.RED + "Current instruction seems to be null! I think that is your problem!")
        sys.error(e.getClass.getName + " was thrown in the game loop. Game cancelled!")
        e.printStackTrace()
      }
    }
    for(player <- this.map.getPlayers){
      if(player.getEntity.isDefined) player.getEntity.get.setSpawnChunk(null, false)
      if(TeamspeakManager.isEnabled && player.getTeamspeakName != null)
        TeamspeakManager.moveClientToChannel(TeamspeakManager.getClientForUser(player.getTeamspeakName).get, 14) //FIXME: lobby!
    }
    this.gameRunning = false
  }

  @inline def getMap = this.map
  @inline def isGameRunning = this.gameRunning

  @inline def setWatchUnready(watch: Boolean) = this.watchUnready = watch
  @inline def setWinnerInterrupt(interrupt: Boolean) = this.interruptWin = interrupt
  def setWinner(team: Team){
    if(this.winner != null && !this.interruptWin) return
    this.winner = team
    this.gameRunning = false
    StatManager.enableStat("gamewon")
    this.getMap.sendMessageToAllPlayers(EnumColor.GOLD + "Winning team: " + this.winner.toString)
  }

  def notifyReadyUpdate(){
    var allReady = true
    this.map.getTeamManager.getTeams.filter(t => !t.isReady).foreach(t => allReady = false)
    if(!allReady && this.watchUnready && this.gameRunning){
      this.gameRunning = false
      this.getMap.sendMessageToAllPlayers("The game was stopped because not all teams are ready")
    }else if(allReady && !this.gameRunning){
      this.start()
    }
  }
}
