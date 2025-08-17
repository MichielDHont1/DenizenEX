package com.denizenscript.denizen.objects.properties.inventory;

import com.denizenscript.denizen.objects.InventoryTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.ObjectProperty;

public class InventorySize extends ObjectProperty<InventoryTag, ElementTag> {

    // <--[property]
    // @object InventoryTag
    // @name size
    // @input ElementTag(Number)
    // @description
    // Controls the size of the inventory.
    // Note that the mechanism can only be set for "generic" chest inventories.
    // -->

    public static boolean describes(ObjectTag inventory) {
        return true;
    }

    public int getSize() {
        if (object.getInventory() == null) {
            return 0;
        }
        return object.getInventory().getContainerSize();
    }

    public void setSize(int size) {
//todo setcontainer and copy contents
        //        object.setSize(size);
    }

    @Override
    public boolean isDefaultValue(ElementTag size) {
        //todo
        return false;
//        return !(getSize() > 0 && (object.getIdType().equals("generic") || object.getIdType().equals("script")) && object.getInventoryType() == InventoryType.CHEST);
    }

    @Override
    public ElementTag getPropertyValue() {
        return new ElementTag(getSize());
    }

    @Override
    public void setPropertyValue(ElementTag param, Mechanism mechanism) {
        if (object.getIdType().equals("generic") || object.getIdType().equals("script")) {
            setSize(param.asInt());
        }
        else {
            mechanism.echoError("Inventories of type '" + object.getIdType() + "' cannot have their size changed!");
        }
    }

    @Override
    public String getPropertyId() {
        return "size";
    }

    public static void register() {
        autoRegister("size", InventorySize.class, ElementTag.class, false);
    }
}
