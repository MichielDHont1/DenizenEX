package com.denizenscript.denizen.utilities.Persistence;

import com.denizenscript.denizen.utilities.NamespacedKey;
import com.denizenscript.denizencore.objects.ObjectTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;

public interface PersistentDataInterface extends INBTSerializable<CompoundTag> {
    String getString(NamespacedKey key);;
    boolean has(NamespacedKey key);
    void set(NamespacedKey key, ObjectTag value);
    void remove(NamespacedKey key);
}