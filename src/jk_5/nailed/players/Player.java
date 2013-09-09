package jk_5.nailed.players;

import jk_5.nailed.Nailed;
import jk_5.nailed.groups.Group;
import jk_5.nailed.map.Map;
import jk_5.nailed.teams.Team;
import jk_5.nailed.teamspeak3.TeamspeakClient;
import jk_5.nailed.util.EnumColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

/**
 * Utility class that is assigned to each player when they join the server
 * It is used to store data and to perform actions
 *
 * @author jk-5
 */
public class Player {

    private String username;
    private TeamspeakClient teamspeakClient;
    private Map currentMap;
    private Team team = Team.UNKNOWN;
    private Group group = Nailed.groupRegistry.getDefaultGroup();
    private boolean spectator = false;

    public Player(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public Group getGroup() {
        return this.group;
    }

    public void onLogin() {
        if (!Nailed.config.getTag("teamspeak").getTag("enabled").getBooleanValue(false)) return;
        if (this.teamspeakClient != null) return;
        this.setTeamspeakClient(Nailed.teamspeak.getClientForUser(this.username));
    }

    public EntityPlayerMP getEntity() {
        return MinecraftServer.getServer().getConfigurationManager().getPlayerEntity(this.username);
    }

    public void setTeamspeakClient(TeamspeakClient ts) {
        this.teamspeakClient = ts;
        if (this.teamspeakClient != null) {
            this.sendChatMessage(EnumColor.AQUA + "You are now linked to your teamspeak account " + this.teamspeakClient.getNickname());
        } else {
            this.sendChatMessage(EnumColor.AQUA + "There was no teamspeak client found with the same username as you. Change your teamspeak username so it matches your ingame name or do /ts setname");
        }
    }

    public TeamspeakClient getTeamspeakClient() {
        return this.teamspeakClient;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setTeam(Team team) {
        if (this.team != null) {
            this.currentMap.getWorld().getScoreboard().func_96524_g(this.username); //leave
        }
        if (team.scoreboardTeam != null) {
            this.currentMap.getWorld().getScoreboard().func_96521_a(this.username, team.scoreboardTeam);
        }
        this.team = team;
    }

    public Team getTeam() {
        return this.team;
    }

    public String getChatFormattedName() {
        return this.group.getNameColor() + this.getUsername() + EnumColor.RESET.toString();
    }

    public String formatChatMessage(String message) {
        return String.format("%s%s%s%s%s[%s%s] %s", this.getTeam().toChatFormatString(), this.spectator ? EnumColor.AQUA + "[Spectator] " : "", EnumColor.GREY, this.getGroup().getChatPrefix(), EnumColor.GREY, this.getChatFormattedName(), EnumColor.GREY, message);
    }

    public void sendChatMessage(String message) {
        if (this.getEntity() != null) this.getEntity().sendChatToPlayer(ChatMessageComponent.func_111066_d(message));
    }

    public void sendPacket(Packet packet) {
        this.getEntity().playerNetServerHandler.sendPacket(packet);
    }

    public void playSound(String name, float volume, float pitch) {
        EntityPlayerMP p = this.getEntity();
        if (p == null) return;
        this.sendPacket(new Packet62LevelSound(name, p.posX, p.posY, p.posZ, volume, pitch));
    }

    public boolean isSpectator() {
        return this.spectator;
    }

    public void setSpectator(boolean spectator) {
        if (this.spectator == spectator) return;
        if (this.getTeam() != Team.UNKNOWN && this.currentMap.getGameThread().isGameRunning()) {
            this.sendChatMessage(EnumColor.RED + "You can not join spectator mode while you are in a game!");
            return;
        }
        EntityPlayer entity = this.getEntity();
        if (spectator) {
            this.spectator = true;
            getScoreboardFromWorldServer().func_96521_a(this.username, this.currentMap.getTeamManager().spectatorTeam); //join
            entity.addPotionEffect(new PotionEffect(Potion.invisibility.getId(), 1000000, 0, true));
            entity.capabilities.allowEdit = false;
            entity.capabilities.disableDamage = true;
            entity.capabilities.allowFlying = true;
            entity.capabilities.isFlying = true;
            entity.sendPlayerAbilities();
            this.sendChatMessage(EnumColor.GREEN + "You are in spectator mode. To disable, type " + EnumColor.YELLOW + "/spectator" + EnumColor.GREEN + " again");
        } else {
            this.spectator = false;
            getScoreboardFromWorldServer().func_96524_g(this.username); //leave
            entity.removePotionEffect(Potion.invisibility.getId());
            entity.capabilities.allowEdit = true;
            entity.capabilities.disableDamage = false;
            entity.capabilities.allowFlying = false;
            entity.capabilities.isFlying = false;
            entity.sendPlayerAbilities();
            this.sendChatMessage(EnumColor.GREEN + "You are no longer in spectator mode");
        }
    }

    private static Scoreboard getScoreboardFromWorldServer() {
        return MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
    }

    public void setCurrentMap(Map map) {
        this.currentMap = map;
    }

    public Map getCurrentMap() {
        return this.currentMap;
    }
}
