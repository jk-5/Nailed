package jk_5.nailed.map.gameloop.instructions;

import jk_5.nailed.map.gameloop.GameThread;
import jk_5.nailed.map.gameloop.IInstruction;
import net.minecraft.src.WorldServer;

/**
 * No description given
 *
 * @author jk-5
 */
public class InstructionSetDifficulty implements IInstruction {

    private int difficulty;

    @Override
    public void injectArguments(String arguments) {
        this.difficulty = Integer.parseInt(arguments);
    }

    @Override
    public void execute(GameThread controller) throws InterruptedException {
        WorldServer server = controller.getMap().getWorld();
        server.difficultySetting = this.difficulty;
        server.setAllowedSpawnTypes(controller.getMap().getMappack().shouldSpawnHostileMobs() && this.difficulty > 0, controller.getMap().getMappack().shouldSpawnFriendlyMobs());
    }
}
