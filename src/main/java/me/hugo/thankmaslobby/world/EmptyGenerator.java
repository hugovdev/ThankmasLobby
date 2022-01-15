package me.hugo.thankmaslobby.world;

import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.batch.ChunkBatch;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EmptyGenerator implements ChunkGenerator {

    @Override
    public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
        /*for (byte x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
            for (byte z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                for (byte y = 0; y < 40; y++) {
                    batch.setBlock(x, y, z, Block.STONE);
                }
            }
        }*/
    }

    @Override
    public List<ChunkPopulator> getPopulators() {
        return null;
    }
}