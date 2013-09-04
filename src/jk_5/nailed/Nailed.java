package jk_5.nailed;

import com.google.common.eventbus.EventBus;
import jk_5.nailed.command.*;
import jk_5.nailed.config.helper.ConfigFile;
import jk_5.nailed.groups.GroupAdmin;
import jk_5.nailed.groups.GroupPlayer;
import jk_5.nailed.groups.GroupRegistry;
import jk_5.nailed.irc.IrcConnector;
import jk_5.nailed.map.MapManager;
import jk_5.nailed.map.stats.StatManager;
import jk_5.nailed.multiworld.MultiworldManager;
import jk_5.nailed.network.IPCClient;
import jk_5.nailed.players.PlayerRegistry;
import jk_5.nailed.teams.TeamRegistry;
import jk_5.nailed.teamspeak3.TeamspeakManager;
import jk_5.nailed.util.EnumColor;
import net.minecraft.src.CommandHandler;
import net.minecraft.src.DedicatedServer;
import net.minecraft.src.EnumGameType;

import java.io.File;

/**
 * Main nailed class. Controls the initialization and contains all helpers that are needed for the server
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
    public static final StatManager statManager = new StatManager();
    public static final IrcConnector irc = new IrcConnector();
    public static final IPCClient ipc = new IPCClient();
    public static final TeamspeakManager teamspeak = new TeamspeakManager();
    public static DedicatedServer server;

    public static void init(DedicatedServer server) {
        Nailed.server = server;

        server.setMOTD(EnumColor.GREEN + "[Nail]" + EnumColor.RESET + " Ohai!");

        eventBus.register(new NailedEventListener());
        eventBus.register(playerRegistry);

        multiworldManager.init();

        mapManager.readMapConfig();

        groupRegistry.registerGroup("player", new GroupPlayer());
        groupRegistry.registerGroup("admin", new GroupAdmin());
        groupRegistry.setDefaultGroup("player");

        Nailed.multiworldManager.setDefaultMapID(0);

        irc.connect();
        ipc.start();
        teamspeak.connect();
    }

    public static void onWorldReady() {
        teamRegistry.setupTeams();

        //WorldServer world1 = Nailed.multiworldManager.createNewMapDimension(1);
        //Nailed.multiworldManager.prepareSpawnForWorld(1);

        server.setCanSpawnAnimals(mapManager.getConfig().getProperty("spawn-animals", "true").equalsIgnoreCase("true"));
        server.setCanSpawnNPCs(mapManager.getConfig().getProperty("spawn-npcs", "true").equalsIgnoreCase("true"));
        server.setAllowPvp(mapManager.getConfig().getProperty("pvp", "true").equalsIgnoreCase("true"));
        server.setTexturePack(mapManager.getConfig().getProperty("texture-pack", ""));
        server.setGameType(EnumGameType.getByID(Integer.parseInt(mapManager.getConfig().getProperty("default-gamemode", Integer.toString(EnumGameType.SURVIVAL.getID())))));
        server.setAllowFlight(true);
        server.func_104055_i(false);  //setForceGamemode
    }

    public static void registerCommands(CommandHandler handler) {
        handler.registerCommand(new CommandCB());
        handler.registerCommand(new CommandGroup());
        handler.registerCommand(new CommandTeam());
        handler.registerCommand(new CommandNewWorld());
        handler.registerCommand(new CommandSpectator());
        handler.registerCommand(new CommandStartGame());
    }
}
