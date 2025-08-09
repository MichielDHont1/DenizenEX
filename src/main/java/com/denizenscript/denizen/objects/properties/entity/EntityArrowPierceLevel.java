package com.denizenscript.denizen.objects.properties.entity;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.core.ElementTag;
import net.minecraft.world.entity.projectile.AbstractArrow;

public class EntityArrowPierceLevel extends EntityProperty<ElementTag> {

    // <--[property]
    // @object EntityTag
    // @name pierce_level
    // @input ElementTag(Number)
    // @description
    // The number of entities an arrow will pierce through while flying. Must be between 0 and 127.
    // -->

    public static boolean describes(EntityTag entity) {
        return entity.getBukkitEntity() instanceof AbstractArrow;
    }

    @Override
    public ElementTag getPropertyValue() {
        return new ElementTag(as(AbstractArrow.class).getPierceLevel());
    }

    @Override
    public void setPropertyValue(ElementTag value, Mechanism mechanism) {
        if (mechanism.requireInteger()) {
            as(AbstractArrow.class).setPierceLevel((byte)value.asInt());
        }
    }

    @Override
    public String getPropertyId() {
        return "pierce_level";
    }

    public static void register() {
        autoRegister("pierce_level", EntityArrowPierceLevel.class, ElementTag.class, false);
    }
}
