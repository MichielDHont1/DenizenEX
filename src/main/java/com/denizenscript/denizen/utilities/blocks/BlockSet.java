package com.denizenscript.denizen.utilities.blocks;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizen.utilities.Location;

import java.util.HashSet;
import java.util.List;

public interface BlockSet {

    class InputParams {

        public Location centerLocation;

        public boolean noAir;

//        public HashSet<Material> mask;

        public List<PlayerTag> fakeTo;

        public DurationTag fakeDuration;
    }

//    FullBlockData[] getBlocks();

    void setBlocksDelayed(final Runnable runme, final InputParams input, long maxDelayMs);

    void setBlocks(InputParams input);
}
