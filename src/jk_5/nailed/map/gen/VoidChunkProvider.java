package jk_5.nailed.map.gen;

import net.minecraft.src.*;

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

    private void generate(byte[] par1ArrayOfByte) {
    }

    public Chunk loadChunk(int par1, int par2) {
        return this.provideChunk(par1, par2);
    }

    public void populate(IChunkProvider par1IChunkProvider, int par2, int par3) {
    }

    public Chunk provideChunk(int par1, int par2) {
        Chunk ret = new Chunk(world, new byte[32768], par1, par2);
        BiomeGenBase[] var5 = world.getWorldChunkManager().loadBlockGeneratorData(null, par1 * 16, par2 * 16, 16, 16);
        ret.generateSkylightMap();
        return ret;
    }
}
