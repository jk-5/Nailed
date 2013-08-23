package jk_5.nailed;

import com.google.common.eventbus.EventBus;
import jk_5.nailed.command.CommandTest;
import jk_5.nailed.multiworld.MultiworldManager;
import jk_5.nailed.players.PlayerRegistry;
import jk_5.nailed.teams.TeamRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandHandler;
import net.minecraft.src.ICommand;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class Nailed {
    public static final EventBus eventBus = new EventBus();
    public static final TeamRegistry teamRegistry = new TeamRegistry();
    public static final PlayerRegistry playerRegistry = new PlayerRegistry();
    public static final MultiworldManager multiworldManager = new MultiworldManager();

    public static void init(){
        eventBus.register(new NailedEventListener());
        eventBus.register(playerRegistry);
    }

    public static void registerCommands(CommandHandler handler){
        handler.registerCommand(new CommandTest());
    }
}
