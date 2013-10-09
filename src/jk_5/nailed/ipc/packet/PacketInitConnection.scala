package jk_5.nailed.ipc.packet

import com.nexus.data.json.{JsonArray, JsonObject}
import jk_5.nailed.Nailed
import scala.collection.JavaConversions._
import jk_5.nailed.players.PlayerRegistry
import jk_5.nailed.map.MapLoader

/**
 * No description given
 *
 * @author jk-5
 */
class PacketInitConnection extends IPCPacket {

  def write(data: JsonObject){
    val players = new JsonArray
    PlayerRegistry.getPlayers.foreach(p => players.add(p.getUsername))
    val mappacks = new JsonArray
    MapLoader.getMappacks.foreach(m => mappacks.add(m.mapName))
    data.add("players", players).add("mappacks", mappacks)
  }
  @inline def read(data: JsonObject) = NoOp
  @inline def process() = NoOp
}
