package jk_5.nailed.network.packet;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nexus.data.json.JsonObject;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class Packet {

    private static BiMap<Integer, Class<? extends Packet>> packetMap = HashBiMap.create();

    static {
        packetMap.put(21, Packet21PlayerJoin.class);
        packetMap.put(22, Packet22PlayerLeave.class);
    }

    public abstract void writePacketData(JsonObject data);

    public abstract void readPacketData(JsonObject data);

    public abstract void processPacket();

    public int getPacketID() {
        return packetMap.inverse().get(this.getClass());
    }

    public JsonObject getSendPacket() {
        JsonObject writtenData = new JsonObject();
        this.writePacketData(writtenData);
        JsonObject data = new JsonObject();
        data.add("id", this.getPacketID());
        if (!writtenData.isEmpty()) data.add("data", writtenData);
        return data;
    }

    public static Packet getPacket(int id) {
        try {
            return packetMap.get(id).newInstance();
        } catch (Exception e) {
        }
        return null;
    }
}
