package jk_5.nailed.map

import net.minecraft.src._
import java.util
import java.util.Random
import jk_5.nailed.NailedEventFactory

/**
 * No description given
 *
 * @author jk-5
 */
object NailedWorldGen {
  @inline def getChunkProvider(world: World) = new VoidChunkProvider(world)
  @inline def getWorldChunkManager(world: World) = new VoidWorldChunkManager(world)
}

class VoidChunkProvider(private final val world: World) extends ChunkProviderFlat(world, world.getSeed, false, null) {
  @inline override def loadChunk(x: Int, z: Int) = this.provideChunk(x, z)
  override def provideChunk(x: Int, z: Int): Chunk = {
    val ret = new Chunk(this.world, new Array[Byte](32768), x, z)
    this.world.getWorldChunkManager.loadBlockGeneratorData(null, x * 16, z * 16, 16, 16)
    ret.generateSkylightMap()
    ret
  }
}

class VoidWorldChunkManager(private final val world: World) extends WorldChunkManager {
  override def findBiomePosition(x: Int, z: Int, range: Int, biomes: util.List[_], rand: Random): ChunkPosition = {
    var ret = super.findBiomePosition(x, z, range, biomes, rand)
    if (x == 0 && z == 0 && !world.getWorldInfo.isInitialized) {
      if (ret == null) ret = new ChunkPosition(0, 0, 0)
      val spawn = NailedEventFactory.getSpawnPoint(this.world)
      world.setBlock(spawn.posX, spawn.posY, spawn.posZ, Block.bedrock.blockID)
      printf("Building spawn platform at: %d, %d, %d\n", spawn.posX, spawn.posY, spawn.posZ)
    }
    ret
  }
}