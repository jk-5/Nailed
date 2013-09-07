package jk_5.nailed.map.gen;

import net.minecraft.src.IChunkProvider;
import net.minecraft.src.World;
import net.minecraft.src.WorldChunkManager;
import net.minecraft.src.WorldType;

/**
 * No description given
 *
 * @author jk-5
 */
public class NailedWorldGen {

    public static WorldType getWorldType() {
        return new VoidWorldType(3); //TODO: resolve ID
    }

    public static IChunkProvider getChunkProvider(World world) {
        return new VoidChunkProvider(world);
    }

    public static WorldChunkManager getWorldChunkManager(World world) {
        return new VoidWorldChunkManager(world);
    }
}
