package jk_5.nailed;

import com.google.common.eventbus.EventBus;
import jk_5.nailed.command.*;
import jk_5.nailed.config.helper.ConfigFile;
import jk_5.nailed.groups.GroupAdmin;
import jk_5.nailed.groups.GroupPlayer;
import jk_5.nailed.groups.GroupRegistry;
import jk_5.nailed.ipc.IPCClient;
import jk_5.nailed.irc.IrcConnector;
import jk_5.nailed.map.Map;
import jk_5.nailed.map.MapLoader;
import jk_5.nailed.map.stats.StatManager;
import jk_5.nailed.players.PlayerRegistry;
import jk_5.nailed.teamspeak3.TeamspeakManager;
import net.minecraft.src.CommandHandler;
import net.minecraft.src.DedicatedServer;

import java.io.File;

/**
 * Main nailed class. Controls the initialization and contains all helpers that are needed for the server
 *
 * @author jk-5
 */
public class Nailed {
    public static final EventBus eventBus = new EventBus();
    public static final GroupRegistry groupRegistry = new GroupRegistry();
    public static final ConfigFile config = new ConfigFile(new File("nailed.cfg")).setComment("Nailed main config file");
    public static final MapLoader mapLoader = new MapLoader();
    public static final StatManager statManager = new StatManager();
    public static final IrcConnector irc = new IrcConnector();
    public static final TeamspeakManager teamspeak = new TeamspeakManager();
    public static DedicatedServer server;

    public static void init(DedicatedServer server) {
        Nailed.server = server;

        eventBus.register(NailedEventListener$.MODULE$);

        mapLoader.loadMaps();

        groupRegistry.registerGroup("player", new GroupPlayer());
        groupRegistry.registerGroup("admin", new GroupAdmin());
        groupRegistry.setDefaultGroup("player");

        mapLoader.setupLobby();

        irc.connect();
        IPCClient.start();

        //teamspeak.setEnabled(false); //Disable it, it's broke like a joke
        teamspeak.connect();
    }

    public static void onWorldReady() {
        Map map1 = mapLoader.createWorld(mapLoader.getMappack("normalLobby"));
        Map map2 = mapLoader.createWorld(mapLoader.getMappack("raceforwool"));

        mapLoader.setupMapSettings();
    }

    public static void registerCommands(CommandHandler handler) {
        handler.registerCommand(CommandCB$.MODULE$);
        handler.registerCommand(new CommandGroup());
        handler.registerCommand(new CommandTeam());
        handler.registerCommand(new CommandNewWorld());
        handler.registerCommand(new CommandSpectator());
        handler.registerCommand(new CommandStartGame());
        handler.registerCommand(CommandBroadcastChat$.MODULE$);
        handler.registerCommand(new CommandTeamleader());
        handler.registerCommand(new CommandReady());
        handler.registerCommand(new CommandServerMode());
        handler.registerCommand(CommandReconnectIPC$.MODULE$);
        if (teamspeak.isEnabled()) handler.registerCommand(new CommandTeamspeak());
    }
}
