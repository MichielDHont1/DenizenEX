package com.denizenscript.denizen.objects.properties.entity;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Mechanism;
import net.minecraft.world.entity.decoration.ArmorStand;

public class EntityArms extends EntityProperty<ElementTag> {

    // <--[property]
    // @object EntityTag
    // @name arms
    // @input ElementTag(Boolean)
    // @description
    // Controls whether an armor stand has arms.
    // -->

    public static boolean describes(EntityTag entity) {
        return entity.getBukkitEntity() instanceof ArmorStand;
    }

    @Override
    public boolean isDefaultValue(ElementTag val) {
        return !val.asBoolean();
    }

    @Override
    public ElementTag getPropertyValue() {
        return new ElementTag(as(ArmorStand.class).isShowArms());
    }

    @Override
    public void setPropertyValue(ElementTag param, Mechanism mechanism) {
        //todo
//        if (mechanism.requireBoolean()) {
//            as(ArmorStand.class).setShowArms(param.asBoolean());
//        }
    }

    @Override
    public String getPropertyId() {
        return "arms";
    }

    public static void register() {
        autoRegister("arms", EntityArms.class, ElementTag.class, false);
    }
}
