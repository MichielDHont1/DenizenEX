package com.denizenscript.denizen.utilities.Persistence;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraftforge.event.AttachCapabilitiesEvent;

@Mod.EventBusSubscriber(modid = "denizenex", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PersistentDataAttacher {

    private static class PersistentDataProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        public static final ResourceLocation IDENTIFIER = ResourceLocation.fromNamespaceAndPath("denizenex", "persistentdata");

        private final PersistentDataInterface backend = new PersistentDataImplementation();
        private final LazyOptional<PersistentDataInterface> optionalData = LazyOptional.of(() -> backend);

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return PersistentDataCapability.INSTANCE.orEmpty(cap, this.optionalData);
        }

        void invalidate() {
            this.optionalData.invalidate();
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.backend.deserializeNBT(nbt);
        }
    }
    @SubscribeEvent
    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        final PersistentDataProvider provider = new PersistentDataProvider();
        event.addCapability(PersistentDataProvider.IDENTIFIER, provider);
    }

    private PersistentDataAttacher() {
    }
}