package com.denizenscript.denizen.nms.interfaces.packets;

import net.minecraft.world.item.ItemStack;

public interface PacketOutWindowItems {

    ItemStack[] getContents();

    void setContents(ItemStack[] contents);
}
