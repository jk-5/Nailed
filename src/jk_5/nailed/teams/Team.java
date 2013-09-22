package jk_5.nailed.teams;

import com.google.common.collect.Lists;
import jk_5.nailed.Nailed;
import jk_5.nailed.map.Map;
import jk_5.nailed.players.Player;
import jk_5.nailed.util.EnumColor;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ScorePlayerTeam;

import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public class Team {

    public static Team UNKNOWN = new Team("Unknown Team", "unknown", EnumColor.GREY);

    private String name;
    private String teamId;
    private EnumColor color;
    private Player leader = null;
    private boolean ready = false;
    private boolean friendlyFire = false;
    public ScorePlayerTeam scoreboardTeam = null;

    private Map map;
    private ChunkCoordinates spawn = null;

    public Team(String name, String teamId, EnumColor color) {
        this.name = name;
        this.teamId = teamId;
        this.color = color;
    }

    public EnumColor getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }

    public String getTeamID() {
        return this.teamId;
    }

    public void sendChatMessage(String message) {
        for (Player p : Nailed.playerRegistry.getPlayers()) {
            if (p.getTeam() == this) {
                p.sendChatMessage(message);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%s%s%s", this.color.toString(), this.name, EnumColor.RESET);
    }

    public String toChatFormatString() {
        if (this == Team.UNKNOWN) return "";
        return String.format("%s* %s%s ", this.getColor().toString(), this.getName(), EnumColor.RESET);
    }

    public void setMap(Map map){
        this.map = map;
    }

    public void setSpawnpoint(ChunkCoordinates coordinates){
        this.spawn = coordinates;
    }

    public boolean shouldOverrideSpawnpoint(){
        if(this.map == null) return false;
        if(this.spawn == null) return false;
        return true;
    }

    public ChunkCoordinates getSpawnpoint(){
        if(!this.shouldOverrideSpawnpoint()) return null;
        else return this.spawn;
    }

    public Player getTeamLeader(){
        return this.leader;
    }

    public void setTeamLeader(Player player){
        this.leader = player;
    }

    public List<String> getAllPlayerNames(){
        List<String> ret = Lists.newArrayList();
        for(Player p : Nailed.playerRegistry.getPlayers()){
            if(p.getTeam() == this) ret.add(p.getUsername());
        }
        return ret;
    }

    public boolean isReady(){
        return this.ready;
    }

    public void setReady(boolean ready){
        this.ready = ready;
        if(this.isReady()){
            this.map.sendMessageToAllPlayers("Team " + this.toString() + " is ready!");
        }else{
            this.map.sendMessageToAllPlayers("Team " + this.toString() + " is not ready!");
        }
        this.map.getGameThread().notifyReadyUpdate();
    }

    public boolean friendlyFireEnabled(){
        return this.friendlyFire;
    }

    public void setFriendlyFireEnabled(boolean on){
        this.friendlyFire = on;
    }
}
