package jk_5.nailed.players;

import jk_5.nailed.Nailed;
import jk_5.nailed.groups.Group;
import jk_5.nailed.teams.Team;
import jk_5.nailed.teams.TeamRegistry;
import jk_5.nailed.util.EnumColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class Player {

    private String username;
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

    public EntityPlayer getEntity() {
        return MinecraftServer.getServer().getConfigurationManager().getPlayerEntity(this.username);
    }

    public void setGroup(Group group) {
        EntityPlayer entity = this.getEntity();
        this.group.onPlayerLeave(entity);
        this.group = group;
        this.group.onPlayerJoin(entity);
    }

    public Team getTeam() {
        return Nailed.teamRegistry.getTeamFromPlayer(username);
    }

    public String getChatFormattedName() {
        return this.group.getNameColor() + this.getUsername() + EnumColor.RESET.toString();
    }

    public String formatChatMessage(String message) {
        return String.format("%s%s%s%s%s[%s%s] %s", this.getTeam().toChatFormatString(), this.spectator ? EnumColor.AQUA + "[Spectator] " : "", EnumColor.GREY, this.getGroup().getChatPrefix(), EnumColor.GREY, this.getChatFormattedName(), EnumColor.GREY, message);
    }

    public void sendChatMessage(String message) {
        MinecraftServer.getServer().getConfigurationManager().getPlayerEntity(this.username).sendChatToPlayer(ChatMessageComponent.func_111066_d(message));
    }

    public boolean isSpectator() {
        return this.spectator;
    }

    public void setSpectator(boolean spectator) {
        if (this.spectator == spectator) return;
        EntityPlayer entity = this.getEntity();
        if (spectator) {
            this.spectator = true;
            getScoreboardFromWorldServer().func_96521_a(this.username, TeamRegistry.spectatorTeam); //join
            entity.addPotionEffect(new PotionEffect(Potion.invisibility.getId(), 1000000, 0, true));
            entity.capabilities.allowEdit = false;
            entity.capabilities.disableDamage = true;
            entity.capabilities.allowFlying = true;
            entity.capabilities.isFlying = true;
            this.sendChatMessage(EnumColor.GREEN + "You are in spectator mode. To disable, type " + EnumColor.YELLOW + "/spectator" + EnumColor.GREEN + " again");
        } else {
            this.spectator = false;
            getScoreboardFromWorldServer().func_96524_g(this.username); //leave
            entity.removePotionEffect(Potion.invisibility.getId());
            entity.capabilities.allowEdit = true;
            entity.capabilities.disableDamage = false;
            entity.capabilities.allowFlying = false;
            entity.capabilities.isFlying = false;
            this.sendChatMessage(EnumColor.GREEN + "You are no longer in spectator mode");
        }
    }

    private static Scoreboard getScoreboardFromWorldServer() {
        return MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
    }
}
