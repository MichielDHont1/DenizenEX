package com.denizenscript.denizen.nms.interfaces.packets;

import net.minecraft.world.item.ItemStack;

public interface PacketOutSetSlot {

    ItemStack getItemStack();

    void setItemStack(ItemStack itemStack);
}
