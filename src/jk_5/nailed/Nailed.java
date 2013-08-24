package jk_5.nailed;

import com.google.common.eventbus.EventBus;
import jk_5.nailed.command.CommandGroup;
import jk_5.nailed.config.NailedConfig;
import jk_5.nailed.config.helper.ConfigFile;
import jk_5.nailed.config.helper.Configuration;
import jk_5.nailed.groups.GroupAdmin;
import jk_5.nailed.groups.GroupPlayer;
import jk_5.nailed.groups.GroupRegistry;
import jk_5.nailed.map.MapManager;
import jk_5.nailed.multiworld.MultiworldManager;
import jk_5.nailed.players.PlayerRegistry;
import jk_5.nailed.teams.TeamRegistry;
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
    public static DedicatedServer server;

    public static void init(DedicatedServer server){
        Nailed.server = server;

        server.setMOTD("Next game in 4 minutes");

        eventBus.register(new NailedEventListener());
        eventBus.register(playerRegistry);

        groupRegistry.registerGroup("player", new GroupPlayer());
        groupRegistry.registerGroup("admin", new GroupAdmin());
        groupRegistry.setDefaultGroup("player");

        mapManager.setupSpawnMap();
    }

    public static void registerCommands(CommandHandler handler){
        handler.registerCommand(new CommandGroup());
    }
}
