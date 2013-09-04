package jk_5.nailed.map.gen;

import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkProviderFlat;
import net.minecraft.src.World;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class VoidChunkProvider extends ChunkProviderFlat {

    private World world;

    public VoidChunkProvider(World world) {
        super(world, world.getSeed(), false, null);
        this.world = world;
    }

    @Override
    public Chunk loadChunk(int par1, int par2) {
        return this.provideChunk(par1, par2);
    }

    @Override
    public Chunk provideChunk(int par1, int par2) {
        Chunk ret = new Chunk(world, new byte[32768], par1, par2);
        world.getWorldChunkManager().loadBlockGeneratorData(null, par1 * 16, par2 * 16, 16, 16);
        ret.generateSkylightMap();
        return ret;
    }
}
