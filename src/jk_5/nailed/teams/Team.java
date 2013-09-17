package jk_5.nailed.teams;

import jk_5.nailed.Nailed;
import jk_5.nailed.map.Map;
import jk_5.nailed.players.Player;
import jk_5.nailed.util.EnumColor;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ScorePlayerTeam;

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
}
