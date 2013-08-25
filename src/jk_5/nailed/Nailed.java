package jk_5.nailed;

import com.google.common.eventbus.EventBus;
import jk_5.nailed.command.CommandGroup;
import jk_5.nailed.command.CommandNewWorld;
import jk_5.nailed.command.CommandTeam;
import jk_5.nailed.config.helper.ConfigFile;
import jk_5.nailed.groups.GroupAdmin;
import jk_5.nailed.groups.GroupPlayer;
import jk_5.nailed.groups.GroupRegistry;
import jk_5.nailed.irc.IrcConnector;
import jk_5.nailed.map.MapManager;
import jk_5.nailed.multiworld.MultiworldManager;
import jk_5.nailed.network.IPCClient;
import jk_5.nailed.players.PlayerRegistry;
import jk_5.nailed.teams.TeamRegistry;
import jk_5.nailed.util.EnumColor;
import net.minecraft.src.CommandHandler;
import net.minecraft.src.DedicatedServer;

import java.io.File;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class Nailed {
    public static final EventBus eventBus = new EventBus();
    public static final TeamRegistry teamRegistry = new TeamRegistry();
    public static final PlayerRegistry playerRegistry = new PlayerRegistry();
    public static final GroupRegistry groupRegistry = new GroupRegistry();
    public static final MultiworldManager multiworldManager = new MultiworldManager();
    public static final ConfigFile config = new ConfigFile(new File("nailed.cfg")).setComment("Nailed main config file");
    public static final MapManager mapManager = new MapManager();
    public static final IrcConnector irc = new IrcConnector();
    public static final IPCClient ipc = new IPCClient("minecraft.reening.nl", 7431);
    public static DedicatedServer server;

    public static void init(DedicatedServer server) {
        Nailed.server = server;

        server.setMOTD(EnumColor.GREEN + "[Nail]" + EnumColor.RESET + " Next game in 4 minutes");

        eventBus.register(new NailedEventListener());
        eventBus.register(playerRegistry);

        mapManager.readMapConfig();

        groupRegistry.registerGroup("player", new GroupPlayer());
        groupRegistry.registerGroup("admin", new GroupAdmin());
        groupRegistry.setDefaultGroup("player");

        mapManager.setupSpawnMap();

        irc.connect();
        ipc.start();
    }

    public static void onWorldReady() {
        teamRegistry.setupTeams();

        server.setCanSpawnAnimals(mapManager.getConfig().getProperty("spawn-animals", "true").equalsIgnoreCase("true"));
        server.setCanSpawnNPCs(mapManager.getConfig().getProperty("spawn-npcs", "true").equalsIgnoreCase("true"));
        server.setAllowPvp(mapManager.getConfig().getProperty("pvp", "true").equalsIgnoreCase("true"));
        server.setTexturePack(mapManager.getConfig().getProperty("texture-pack", ""));
    }

    public static void registerCommands(CommandHandler handler) {
        handler.registerCommand(new CommandGroup());
        handler.registerCommand(new CommandTeam());
        handler.registerCommand(new CommandNewWorld());
    }
}
