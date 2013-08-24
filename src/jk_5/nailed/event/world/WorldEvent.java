package jk_5.nailed.event.world;

import jk_5.nailed.event.NailedEvent;
import net.minecraft.src.World;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class WorldEvent extends NailedEvent{

    public final World world;

    public WorldEvent(World world){
        this.world = world;
    }

    public static class Load extends WorldEvent{
        public Load(World world) { super(world); }
    }

    public static class Unload extends WorldEvent{
        public Unload(World world) { super(world); }
    }

    public static class Save extends WorldEvent{
        public Save(World world) { super(world); }
    }
}
