package com.denizenscript.denizen.nms.interfaces;

//import com.denizenscript.denizen.objects.BiomeTag;
import com.denizenscript.denizen.utilities.Location;
import net.minecraft.world.level.Level;

public interface WorldHelper {

    boolean isStatic(Level world);

    void setStatic(Level world, boolean isStatic);

    float getLocalDifficulty(Location location);
//todo
//    default Location getNearestBiomeLocation(Location start, BiomeTag biome) {
//        throw new UnsupportedOperationException();
//    }

    boolean areEnoughSleeping(Level world, int percentage);

    boolean areEnoughDeepSleeping(Level world, int percentage);

    int getSkyDarken(Level world);

    boolean isDay(Level world);

    boolean isNight(Level world);

    /** for setting the time without firing a CUSTOM TimeSkipEvent */
    void setDayTime(Level world, long time);

    void wakeUpAllPlayers(Level world);

    /** for clearing weather without ignoring possible raised event results */
    void clearWeather(Level world);

    default void setGameTime(Level world, long time) {
        throw new UnsupportedOperationException();
    }
}
