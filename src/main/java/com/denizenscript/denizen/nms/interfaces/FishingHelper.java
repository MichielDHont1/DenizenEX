package com.denizenscript.denizen.nms.interfaces;

import com.denizenscript.denizen.utilities.Location;
import org.bukkit.entity.FishHook;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface FishingHelper {

    enum CatchType {NONE, DEFAULT, JUNK, TREASURE, FISH}

    ItemStack getResult(FishHook fishHook, CatchType catchType);

    FishHook spawnHook(Location location, Player player);

    FishHook getHookFrom(Player player);

    void setNibble(FishHook hook, int ticks);

    void setHookTime(FishHook hook, int ticks);

    int getLureTime(FishHook hook);

    void setLureTime(FishHook hook, int ticks);
}
