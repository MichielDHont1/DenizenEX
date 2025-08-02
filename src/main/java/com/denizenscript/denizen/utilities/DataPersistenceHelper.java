package com.denizenscript.denizen.utilities;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.denizen.nms.NMSHandler;
import com.denizenscript.denizen.utilities.Persistence.PersistentDataCapability;
import com.denizenscript.denizen.utilities.Persistence.PersistentDataInterface;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

import java.nio.charset.StandardCharsets;

/**
 * Helper class for PersistentDataContainers.
 */
public class DataPersistenceHelper {


    public static Capability<PersistentDataInterface> PERSISTENT_DATA = CapabilityManager.get(new CapabilityToken<>(){});
//    private static LazyOptional<PersistentDataCapability> cachedCapability = null;
//    private static LazyOptional<PersistentDataCapability> getCapability()
//    {
//        if (cachedCapability == null)
//        {
//            cachedCapability =
//        }
//    }


    public static void removeDenizenKey(ICapabilityProvider holder, String keyName) {
        holder.getCapability(PERSISTENT_DATA).ifPresent(storage -> storage.remove(new NamespacedKey("denizen", keyName)));

    }

    public static void setDenizenKey(ICapabilityProvider holder, String keyName, ObjectTag keyValue) {
        holder.getCapability(PERSISTENT_DATA).ifPresent(storage -> storage.set(new NamespacedKey("denizen", keyName), keyValue));
    }

    public static boolean hasDenizenKey(ICapabilityProvider holder, String keyName) {
        LazyOptional<PersistentDataInterface> LazyCapability = holder.getCapability(PERSISTENT_DATA);
        if (LazyCapability.isPresent())
        {
            return LazyCapability.orElse(null).has(new NamespacedKey("denizen", keyName));
        }
        return false;
    }

    public static ObjectTag getDenizenKey(ICapabilityProvider holder, String keyName) {
        LazyOptional<PersistentDataInterface> LazyCapability = holder.getCapability(PERSISTENT_DATA);
        if (LazyCapability.isPresent())
        {
            String str = LazyCapability.orElse(null).getString(new NamespacedKey("denizen", keyName));
            if (str == null) {
                Debug.echoError("Failed to read ObjectTag from entity key '" + keyName + "' for entity " + ((Entity) holder).getStringUUID() + "...");
                return null;
            }
            return ObjectFetcher.pickObjectFor(str, CoreUtilities.noDebugContext);
        }
        Debug.echoError("Failed to get capability from entity key '" + keyName + "' for entity " + ((Entity) holder).getStringUUID() + "...");
        return null;
    }
}
