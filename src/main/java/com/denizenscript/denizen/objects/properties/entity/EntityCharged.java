package com.denizenscript.denizen.objects.properties.entity;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.core.ElementTag;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.projectile.WitherSkull;

public class EntityCharged extends EntityProperty<ElementTag> {

    // <--[property]
    // @object EntityTag
    // @name charged
    // @input ElementTag(Boolean)
    // @description
    // If the entity is wither_skull, controls whether the skull is charged. Charged skulls are blue.
    // If the entity is a vex, controls whether the vex is charging. Charging vexes have red lines.
    // This is a visual effect, and does not cause the vex to actually charge at anyone.
    // If the entity is a guardian, controls whether the guardian's laser is active.
    // Note that guardians also require a target to use their laser, see <@link command attack>.
    // -->

    public static boolean describes(EntityTag entity) {
        return entity.getBukkitEntity() instanceof WitherSkull
                || entity.getBukkitEntity() instanceof Vex
                || entity.getBukkitEntity() instanceof Guardian;
    }

    @Override
    public boolean isDefaultValue(ElementTag val) {
        return !val.asBoolean();
    }

    @Override
    public ElementTag getPropertyValue() {
        if (getEntity() instanceof WitherSkull entity) {
            return new ElementTag(entity.isDangerous());
        }
        else if (getEntity() instanceof Vex entity) {
            return new ElementTag(entity.isCharging());
        }
        //todo
//        else if (getEntity() instanceof Guardian entity) {
//            return new ElementTag(entity.hasLaser());
//        }
        return null;
    }

    @Override
    public void setPropertyValue(ElementTag param, Mechanism mechanism) {
        if (mechanism.requireBoolean()) {
            if (getEntity() instanceof WitherSkull entity) {
                entity.setDangerous(param.asBoolean());
            }
            else if (getEntity() instanceof Vex entity) {
                entity.setIsCharging(param.asBoolean());
            }
            //todo
//            else if (getEntity() instanceof Guardian entity) {
//                entity.setLaser(param.asBoolean());
//            }
        }
    }

    @Override
    public String getPropertyId() {
        return "charged";
    }

    public static void register() {
        autoRegister("charged", EntityCharged.class, ElementTag.class, false);
    }
}
