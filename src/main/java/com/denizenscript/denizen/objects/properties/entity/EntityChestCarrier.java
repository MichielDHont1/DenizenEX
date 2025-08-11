package com.denizenscript.denizen.objects.properties.entity;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Mechanism;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;

public class EntityChestCarrier extends EntityProperty<ElementTag> {

    // <--[property]
    // @object EntityTag
    // @name carries_chest
    // @input ElementTag(Boolean)
    // @description
    // Controls whether a horse-like entity is carrying a chest.
    // -->

    public static boolean describes(EntityTag entity) {
          return entity.getBukkitEntity() instanceof AbstractChestedHorse;
    }

    @Override
    public ElementTag getPropertyValue() {
        return new ElementTag(as(AbstractChestedHorse.class).hasChest());
    }

    @Override
    public void setPropertyValue(ElementTag param, Mechanism mechanism) {
        if (mechanism.requireBoolean()) {
            as(AbstractChestedHorse.class).setChest(param.asBoolean());
        }
    }

    @Override
    public String getPropertyId() {
        return "carries_chest";
    }

    public static void register() {
        autoRegister("carries_chest", EntityChestCarrier.class, ElementTag.class, false);
    }
}
