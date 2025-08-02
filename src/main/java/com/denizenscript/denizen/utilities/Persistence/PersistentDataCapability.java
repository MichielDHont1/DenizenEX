package com.denizenscript.denizen.utilities.Persistence;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class PersistentDataCapability {
    public static final Capability<PersistentDataInterface> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(PersistentDataInterface.class);
    }

    private PersistentDataCapability() {
    }
}
