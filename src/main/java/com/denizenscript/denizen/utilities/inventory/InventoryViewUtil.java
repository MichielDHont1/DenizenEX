package com.denizenscript.denizen.utilities.inventory;

import com.denizenscript.denizencore.utilities.ReflectionHelper;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import net.minecraft.world.inventory.AbstractContainerMenu;
//import org.bukkit.entity.HumanEntity;
//import org.bukkit.event.inventory.InventoryType;
//import org.bukkit.inventory.Inventory;
//import net.minecraft.world.item.ItemStack;


import java.lang.invoke.MethodHandle;

public class InventoryViewUtil {
    private static <T> T get(MethodHandle methodHandle, AbstractContainerMenu view) {
        try {
            return (T) methodHandle.invoke(view);
        }
        catch (Throwable e) {
            Debug.echoError(e);
            return null;
        }
    }

    private static <T> T get(MethodHandle methodHandle, AbstractContainerMenu view, int num) {
        try {
            return (T) methodHandle.invoke(view, num);
        }
        catch (Throwable e) {
            Debug.echoError(e);
            return null;
        }
    }

    private static final MethodHandle GET_TOP_INVENTORY = ReflectionHelper.getMethodHandle(AbstractContainerMenu.class, "getTopInventory");

    public static Inventory getTopInventory(GuiContainer view) {
        return get(GET_TOP_INVENTORY, view);
    }

    private static final MethodHandle GET_PLAYER = ReflectionHelper.getMethodHandle(GuiContainer.class, "getPlayer");

    public static HumanEntity getPlayer(GuiContainer view) {
        return get(GET_PLAYER, view);
    }

    private static final MethodHandle GET_TYPE = ReflectionHelper.getMethodHandle(GuiContainer.class, "getType");

    public static InventoryType getType(GuiContainer view) {
        return get(GET_TYPE, view);
    }

    private static final MethodHandle GET_ITEM = ReflectionHelper.getMethodHandle(GuiContainer.class, "getItem", int.class);

    public static ItemStack getItem(GuiContainer view, int slot) {
        return get(GET_ITEM, view, slot);
    }

    private static final MethodHandle GET_INVENTORY = ReflectionHelper.getMethodHandle(GuiContainer.class, "getInventory", int.class);

    public static Inventory getInventory(GuiContainer view, int slot) {
        return get(GET_INVENTORY, view, slot);
    }
}
