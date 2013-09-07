package jk_5.nailed.teamspeak3;

import java.util.HashMap;

/**
 * No description given
 *
 * @author jk-5
 */
public class TeamspeakClient {

    private int clientType;
    private int clientID;
    private int channelID;
    private String nickname;
    private String platform;

    public TeamspeakClient(HashMap<String, String> data){
        this.clientType = Integer.parseInt(data.get("client_type"));
        this.clientID = Integer.parseInt(data.get("clid"));
        this.channelID = Integer.parseInt(data.get("cid"));
        this.nickname = data.get("client_nickname");
        this.platform = data.get("client_platform");
    }

    public String getNickname(){
        return this.nickname;
    }

    public int getClientID(){
        return this.clientID;
    }
}
