package com.denizenscript.denizen.nms.interfaces;

import com.denizenscript.denizen.utilities.Location;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface FishingHelper {

    enum CatchType {NONE, DEFAULT, JUNK, TREASURE, FISH}

    ItemStack getResult(FishingHook fishHook, CatchType catchType);

    FishingHook spawnHook(Location location, Player player);

    FishingHook getHookFrom(Player player);

    void setNibble(FishingHook hook, int ticks);

    void setHookTime(FishingHook hook, int ticks);

    int getLureTime(FishingHook hook);

    void setLureTime(FishingHook hook, int ticks);
}
