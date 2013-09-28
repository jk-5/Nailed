package jk_5.nailed.ipc.packet

import com.nexus.data.json.{JsonArray, JsonObject}
import jk_5.nailed.Nailed
import scala.collection.JavaConversions._

/**
 * No description given
 *
 * @author jk-5
 */
class PacketInitConnection extends IPCPacket {

  def write(data: JsonObject){
    val players = new JsonArray
    for(player <- Nailed.playerRegistry.getPlayers){
      players.add(player.getUsername)
    }
    val mappacks = new JsonArray
    for(mappack <- Nailed.mapLoader.getMappacks){
      mappacks.add(mappack.getMapName)
    }
    data.add("players", players).add("mappacks", mappacks)
  }
  @inline def read(data: JsonObject) = NoOp
  @inline def process() = NoOp
}
