package com.denizenscript.denizen.nms.interfaces;

import com.denizenscript.denizen.utilities.Location;
import net.minecraft.world.item.ItemStack;

public interface CustomEntityHelper {

    FakeArrow spawnFakeArrow(Location location);

    ItemProjectile spawnItemProjectile(Location location, ItemStack itemStack);

    FakePlayer spawnFakePlayer(Location location, String name, String skin, String blob, boolean doAdd);
}
