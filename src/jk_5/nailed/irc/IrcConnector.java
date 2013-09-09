package jk_5.nailed.irc;

import com.google.common.eventbus.Subscribe;
import jk_5.nailed.Nailed;
import jk_5.nailed.event.player.PlayerChatEvent;
import jk_5.nailed.event.player.PlayerJoinServerEvent;
import jk_5.nailed.event.player.PlayerLeaveServerEvent;
import jk_5.nailed.players.Player;
import jk_5.nailed.util.EnumColor;
import jk_5.nailed.util.ServerUtils;
import net.minecraft.server.MinecraftServer;
import org.jibble.pircbot.PircBot;

/**
 * No description given
 *
 * @author jk-5
 */
public class IrcConnector extends PircBot implements Runnable {

    public String server = Nailed.config.getTag("irc").getTag("server").setComment("IP/Hostname for the Irc server").getValue("");
    public int port = Nailed.config.getTag("irc").getTag("port").setComment("Port for the Irc server").getIntValue(6667);
    public String serverPassword = Nailed.config.getTag("irc").getTag("serverPassword").setComment("IRC server password").getValue("");
    public String channel = Nailed.config.getTag("irc").getTag("channel").getTag("name").setComment("IRC channel").getValue("#nailed");
    public String channelPass = Nailed.config.getTag("irc").getTag("channel").getTag("password").setComment("IRC channel password").getValue("");

    public IrcConnector() {
        Nailed.eventBus.register(this);
    }

    public void connect() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.setName("IRC Connect thread");
        t.start();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onChat(PlayerChatEvent event) {
        this.sendMessage(this.channel, "<" + event.player.getUsername() + "> " + event.message);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onJoin(PlayerJoinServerEvent event) {
        this.sendMessage(this.channel, "* " + event.player.getUsername() + " has joined the game");
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onLeave(PlayerLeaveServerEvent event) {
        this.sendMessage(this.channel, "* " + event.player.getUsername() + " has left the game");
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.startsWith("!msg")) {
            String args[] = message.split(" ", 3);
            Player p = Nailed.playerRegistry.getPlayer(args[1]);
            if (p == null) this.sendMessage(channel, sender + ": Player " + args[1] + " was not found!");
            else
                p.sendChatMessage(EnumColor.GREY + "[" + channel + "] [" + sender + " -> " + EnumColor.GOLD + p.getUsername() + EnumColor.GREY + "] " + EnumColor.RESET + args[2]);
        } else if (message.equals("!players")) {
            this.sendMessage(channel, "Players currently in game: " + MinecraftServer.getServer().getConfigurationManager().getPlayerListAsString());
        } else
            ServerUtils.broadcastChatMessage(EnumColor.GREY + "[" + channel + "]" + EnumColor.RESET + " <" + sender + "> " + message);
    }

    @Override
    public void onJoin(String channel, String sender, String login, String hostname) {
        ServerUtils.broadcastChatMessage(EnumColor.GREY + "[" + channel + "]" + EnumColor.RESET + " * " + sender + " joined the channel");
    }

    @Override
    public void onPart(String channel, String sender, String login, String hostname) {
        ServerUtils.broadcastChatMessage(EnumColor.GREY + "[" + channel + "]" + EnumColor.RESET + " * " + sender + " left the channel");
    }

    @Override
    public void onNickChange(String oldNick, String login, String hostname, String newNick) {
        ServerUtils.broadcastChatMessage(EnumColor.GREY + "[" + channel + "]" + EnumColor.RESET + " * " + oldNick + " is now known as " + newNick);
    }

    @Override
    public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
        ServerUtils.broadcastChatMessage(EnumColor.GREY + "[" + channel + "]" + EnumColor.RESET + " * " + recipientNick + " was kicked by " + kickerNick + " (" + reason + ")");
    }

    @Override
    public void run() {
        try {
            this.setName("Nailed-Server");
            this.connect(this.server, this.port, this.serverPassword);
            this.joinChannel(this.channel, this.channelPass);
        } catch (Exception e) {
            System.out.println("Could not connect to IRC");
        }
    }
}
