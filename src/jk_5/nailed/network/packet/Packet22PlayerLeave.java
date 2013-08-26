package jk_5.nailed.network.packet;

import com.nexus.data.json.JsonObject;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class Packet22PlayerLeave extends Packet {

    public String username;

    public Packet22PlayerLeave(String username) {
        this.username = username;
    }

    public void writePacketData(JsonObject data) {
        data.add("username", this.username);
    }

    public void readPacketData(JsonObject data) {
    }

    public void processPacket() {
    }
}
