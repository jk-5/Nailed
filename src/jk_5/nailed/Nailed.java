package jk_5.nailed;

import com.google.common.eventbus.EventBus;
import jk_5.nailed.command.CommandGroup;
import jk_5.nailed.groups.GroupAdmin;
import jk_5.nailed.groups.GroupPlayer;
import jk_5.nailed.groups.GroupRegistry;
import jk_5.nailed.multiworld.MultiworldManager;
import jk_5.nailed.players.PlayerRegistry;
import jk_5.nailed.teams.TeamRegistry;
import net.minecraft.src.CommandHandler;

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

    public static void init(){
        eventBus.register(new NailedEventListener());
        eventBus.register(playerRegistry);

        groupRegistry.registerGroup("player", new GroupPlayer());
        groupRegistry.registerGroup("admin", new GroupAdmin());
        groupRegistry.setDefaultGroup("player");
    }

    public static void registerCommands(CommandHandler handler){
        handler.registerCommand(new CommandGroup());
    }
}
