package com.denizenscript.denizen.objects.properties.inventory;

import com.denizenscript.denizen.objects.InventoryTag;
//import com.denizenscript.denizen.scripts.containers.core.InventoryScriptHelper;
//import com.denizenscript.denizen.utilities.PaperAPITools;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.properties.ObjectProperty;
import net.minecraft.world.item.ItemStack;

public class InventoryTitle extends ObjectProperty<InventoryTag, ElementTag> {

    // <--[property]
    // @object InventoryTag
    // @name title
    // @input ElementTag
    // @description
    // Controls the title of the inventory.
    // Note that the mechanism can only be set for "generic" inventories.
    // -->

    public static boolean describes(InventoryTag inventory) {
        return true;
    }

    @Override
    public boolean isDefaultValue(ElementTag title) {
        return !object.isGeneric() && !object.isSaving;
    }

    @Override
    public ElementTag getPropertyValue() {
        //todo
//        if (object.getInventory() != null) {
//            String title = PaperAPITools.instance.getTitle(object.getInventory());
//            if (title != null) {
//                if (!title.startsWith("container.")) {
//                    return new ElementTag(title, true);
//                }
//            }
//        }
        return null;
    }

    @Override
    public void setPropertyValue(ElementTag param, Mechanism mechanism) {
        InventoryTag inventory = object;
        if (!inventory.isGeneric() && !inventory.isUnique()) {
            mechanism.echoError("Cannot set a title on a non-generic inventory.");
            return;
        }
        String title = param.asString();
        //todo menu null check
//        if (InventoryScriptHelper.isPersonalSpecialInv(inventory.getMenu())) {
//            inventory.customTitle = title;
//            return;
//        }
        //todo
//        if (inventory.getInventory() != null && PaperAPITools.instance.getTitle(inventory.getInventory()).equals(title)) {
//            return;
//        }
        inventory.uniquifier = null;
//        if (inventory.getInventory() == null) {
//            inventory.setInventory(PaperAPITools.instance.createInventory(null, InventoryTag.maxSlots, title));
//            InventoryTag.trackTemporaryInventory(inventory);
//            return;
//        }
        ItemStack[] contents = inventory.getContents();
//        if (inventory.getInventory().getType() == InventoryType.CHEST) {
//            inventory.setInventory(PaperAPITools.instance.createInventory(null, inventory.getSize(), title));
//        }
//        else {
//            inventory.setInventory(PaperAPITools.instance.createInventory(null, inventory.getInventory().getType(), title));
//        }
        inventory.setContents(contents);
        InventoryTag.trackTemporaryInventory(inventory);
    }

    @Override
    public String getPropertyId() {
        return "title";
    }

    public static void register() {
        autoRegister("title", InventoryTitle.class, ElementTag.class, false);
    }
}
