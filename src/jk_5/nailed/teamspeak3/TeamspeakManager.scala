package jk_5.nailed.teamspeak3

import jk_5.nailed.Nailed
import jk_5.nailed.teamspeak3.api.JTS3ServerQuery
import jk_5.nailed.teamspeak3.api.TeamspeakActionListener
import java.util
import scala.collection.mutable
import scala.collection.JavaConversions._

/**
 * No description given
 *
 * @author jk-5
 */
object TeamspeakManager extends TeamspeakActionListener {

  private final val server: JTS3ServerQuery = new JTS3ServerQuery
  private var enabled: Boolean = Nailed.config.getTag("teamspeak").getTag("enabled").setComment("Set to false to disable teamspeak integration").getBooleanValue(default = false)
  private final val host: String = Nailed.config.getTag("teamspeak").getTag("host").setComment("IP address / host name for the teamspeak server").getValue("localhost")
  private final val port: Int = Nailed.config.getTag("teamspeak").getTag("port").setComment("Port for the teamspeak query interface").getIntValue(10011)
  private final val username: String = Nailed.config.getTag("teamspeak").getTag("username").setComment("Username for the query interface. Leave empty for none").getValue("")
  private final val password: String = Nailed.config.getTag("teamspeak").getTag("password").setComment("Password for the query interface. Leave empty for none").getValue("")
  private final val clients = mutable.ArrayBuffer[TeamspeakClient]()

  def connect(){
    if (!this.enabled) return
    println("Connecting to teamspeak...")
    if (!this.server.connectTS3Query(this.host, this.port)) {
      System.out.println("Failed!")
      this.enabled = false
      return
    }
    this.server.loginTS3(this.username, this.password)
    if (!this.server.selectVirtualServer(1)) {
      this.displayError()
      this.enabled = false
      return
    }
    this.server.setTeamspeakActionListener(this)
    this.server.addEventNotify(4, 0)
    this.server.addEventNotify(5, 14)
    this.server.addEventNotify(5, 17)
    this.server.addEventNotify(5, 20)
    println("Connected to teamspeak!")
    this.refreshClientList()
  }

  def teamspeakActionPerformed(eventType: String, eventInfo: util.HashMap[String, String]) = this.refreshClientList()

  def moveClientToChannel(client: TeamspeakClient, channelID: Int) {
    if (!server.moveClient(client.getClientID, channelID, null)) {
      println("Error while moving " + client.getNickname)
      println(server.getLastError)
    }
    this.refreshClientList()
  }

  private def refreshClientList(){
    this.clients.clear()
    val dataClientList: util.Vector[util.HashMap[String, String]] = this.server.getList(JTS3ServerQuery.LISTMODE_CLIENTLIST, "-info,-times")
    if (dataClientList == null) return
    dataClientList.foreach(hm => this.clients += new TeamspeakClient(hm))
  }

  @inline def getClientForUser(username: String): Option[TeamspeakClient] = this.clients.find(_.getNickname == username)
  @inline def isEnabled = this.enabled
  @inline def setEnabled(enabled: Boolean) = this.enabled = enabled
  @inline def getNicknames = this.clients.map(_.getNickname)

  private def displayError(){
    val error: String = this.server.getLastError
    if (error != null) {
      System.out.println("Teamspeak error:")
      System.out.println(error)
      if (this.server.getLastErrorPermissionID != -1) {
        val permInfo: util.HashMap[String, String] = this.server.getPermissionInfo(this.server.getLastErrorPermissionID)
        if (permInfo != null) {
          System.out.println("Missing Permission: " + permInfo.get("permname"))
        }
      }
    }
  }
}