package com.denizenscript.denizen.utilities.Persistence;

import com.denizenscript.denizen.utilities.NamespacedKey;
import com.denizenscript.denizencore.objects.ObjectTag;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;

public class PersistentDataImplementation implements PersistentDataInterface {

    private static final String NBT_KEY_DAMAGE_DEALT = "damageDealt";
    private HashMap<String, String> map = new HashMap<String, String>();

    @Override
    public String getString(NamespacedKey key)
    {
        return map.get(key.toString());
    }

    @Override
    public boolean has(NamespacedKey key)
    {
        return map.containsKey(key.toString());
    }

    @Override
    public void set(NamespacedKey key, ObjectTag value)
    {
        map.put(key.toString(),value.toString());
    }

    @Override
    public void remove(NamespacedKey key)
    {
        map.remove(key.toString());
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        for (String key: map.keySet()) tag.putString(key, map.get(key));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        for (String key : nbt.getAllKeys()) map.put(key, nbt.getString(key));
    }
}