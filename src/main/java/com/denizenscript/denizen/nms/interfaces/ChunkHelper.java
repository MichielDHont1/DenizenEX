package com.denizenscript.denizen.nms.interfaces;

//import com.denizenscript.denizen.nms.abstracts.BiomeNMS;
import net.minecraft.world.level.Level;

public interface ChunkHelper {

    default void refreshChunkSections(Chunk chunk) {
        throw new UnsupportedOperationException();
    }

    int[] getHeightMap(Chunk chunk);

    default void changeChunkServerThread(Level world) {
        // Do nothing by default.
    }

    default void restoreServerThread(Level world) {
        // Do nothing by default.
    }

    default void setAllBiomes(Chunk chunk, BiomeNMS biome) {
        throw new UnsupportedOperationException();
    }
}
