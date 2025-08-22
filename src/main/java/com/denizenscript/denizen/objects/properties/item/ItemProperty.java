package com.denizenscript.denizen.objects.properties.item;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.objects.MaterialTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.ObjectProperty;
import net.minecraft.world.item.ItemStack;

public abstract class ItemProperty<TData extends ObjectTag> extends ObjectProperty<ItemTag, TData> {

    public MaterialTag getMaterialTag() {
        return object.getMaterial();
    }

    public ItemStack getItemStack() {
        return object.getItemStack();
    }

    public void setItemStack(ItemStack item) {
        object.setItemStack(item);
    }

}
