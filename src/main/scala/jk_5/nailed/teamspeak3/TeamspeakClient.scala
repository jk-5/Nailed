package jk_5.nailed.teamspeak3

import java.util

/**
 * No description given
 *
 * @author jk-5
 */
class TeamspeakClient(data: util.HashMap[String, String]){
  private final val clientType = data.get("client_type").toInt
  private final val clientID = data.get("clid").toInt
  private final val channelID = data.get("cid").toInt
  private final val nickname = data.get("client_nickname")
  private final val platform = data.get("client_platform")

  @inline def getNickname = this.nickname
  @inline def getClientID = this.clientID
}