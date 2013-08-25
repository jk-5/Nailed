package jk_5.nailed.map.gen;

import jk_5.nailed.NailedEventFactory;
import net.minecraft.src.*;

import java.util.List;
import java.util.Random;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class VoidWorldChunkManager extends WorldChunkManager {

    private World world;

    public VoidWorldChunkManager(World world) {
        super(world);
        this.world = world;
    }

    @Override
    public ChunkPosition findBiomePosition(int x, int z, int range, List biomes, Random rand) {
        ChunkPosition ret = super.findBiomePosition(x, z, range, biomes, rand);
        if (x == 0 && z == 0 && !world.getWorldInfo().isInitialized()) {
            if (ret == null) ret = new ChunkPosition(0, 0, 0);
            ChunkCoordinates spawn = NailedEventFactory.getSpawnPoint();
            world.setBlock(spawn.posX, spawn.posY, spawn.posZ, Block.bedrock.blockID);
            System.out.printf("Building spawn platform at: %d, %d, %d\n", spawn.posX, spawn.posY, spawn.posZ);
        }
        return ret;
    }
}
