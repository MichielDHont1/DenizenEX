package com.denizenscript.denizen.nms.interfaces;

import com.denizenscript.denizen.nms.util.Advancement;
import com.denizenscript.denizen.utilities.Utilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.world.entity.player.Player;

public abstract class AdvancementHelper {

    public static net.minecraft.advancements.Advancement getAdvancement(String name) {
        ResourceLocation location = ResourceLocation.tryParse(name);
        if (location != null)
        {
            return ServerLifecycleHooks.getCurrentServer().getAdvancements().getAdvancement(location);
        }
        else
        {
            return  null;
        }
    }

    public abstract void register(Advancement advancement);

    public abstract void unregister(Advancement advancement);

    public void grantPartial(Advancement advancement, Player player, int len) {
        throw new UnsupportedOperationException();
    }

    public abstract void grant(Advancement advancement, Player player);

    public abstract void revoke(Advancement advancement, Player player);

    public abstract void update(Player player);
}
