package com.denizenscript.denizen.objects.properties.entity;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.objects.Mechanism;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.monster.ZombifiedPiglin;

public class EntityAnger extends EntityProperty<DurationTag> {

    // <--[property]
    // @object EntityTag
    // @name anger
    // @input DurationTag
    // @description
    // Controls the remaining anger time of a PigZombie or Bee.
    // -->

    public static boolean describes(EntityTag entity) {
        return entity.getBukkitEntity() instanceof ZombifiedPiglin
                || entity.getBukkitEntity() instanceof Bee;
    }

    @Override
    public DurationTag getPropertyValue() {
        if (getEntity() instanceof ZombifiedPiglin entity) {
            return new DurationTag((long) entity.getRemainingPersistentAngerTime());
        }
        else if (getEntity() instanceof Bee entity) {
            return new DurationTag((long) entity.getRemainingPersistentAngerTime());
        }
        else {
            return null;
        }
    }

    @Override
    public void setPropertyValue(DurationTag param, Mechanism mechanism) {
        if (mechanism.getValue().isInt()) { // Soft-deprecated - backwards compatibility, as this used to use a tick count
            param = new DurationTag(mechanism.getValue().asLong());
        }
        if (getEntity() instanceof ZombifiedPiglin entity) {
            entity.setRemainingPersistentAngerTime(param.getTicksAsInt());
        }
        else {
            as(Bee.class).setRemainingPersistentAngerTime(param.getTicksAsInt());
        }
    }

    @Override
    public String getPropertyId() {
        return "anger";
    }

    public static void register() {
        autoRegister("anger", EntityAnger.class, DurationTag.class, false);
    }
}
