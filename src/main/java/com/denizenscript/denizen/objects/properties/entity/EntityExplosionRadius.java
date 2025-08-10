package com.denizenscript.denizen.objects.properties.entity;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.utilities.ReflectionHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

public class EntityExplosionRadius implements Property {

    public static boolean describes(ObjectTag entity) {
        return entity instanceof EntityTag
                && (((EntityTag) entity).getBukkitEntity() instanceof PrimedTnt
                || ((EntityTag) entity).getBukkitEntity() instanceof Creeper);
    }

    public static EntityExplosionRadius getFrom(ObjectTag entity) {
        return describes(entity) ? new EntityExplosionRadius((EntityTag) entity) : null;
    }

    public static final String[] handledTags = new String[] {
            "explosion_radius"
    };

    public static final String[] handledMechs = new String[] {
            "explosion_radius"
    };

    public float getExplosionRadius() {
        Entity creeper = entity.getBukkitEntity();
        if (creeper instanceof Creeper) {
            Object obj = ReflectionHelper.getFieldValue(creeper.getClass(), "explosionRadius", creeper);
            if (obj != null)
            {
                return (float) obj;
            }
            return 0;
            //todo remove old code
//            try {
//                Field field = FieldUtils.getField(entity.getBukkitEntityType().getClass(), "explosionRadius", true);
//                return (int) field.get(null);
//            } catch (IllegalAccessException e) {
//                //todo debug msg
//            }
//            return 0;
        }
        return 4; //explosion power is fixed at 4 for tnt entity
    }

    public EntityExplosionRadius(EntityTag ent) {
        entity = ent;
    }

    EntityTag entity;

    @Override
    public String getPropertyString() {
        return String.valueOf(getExplosionRadius());
    }

    @Override
    public String getPropertyId() {
        return "explosion_radius";
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <EntityTag.explosion_radius>
        // @returns ElementTag(Decimal)
        // @mechanism EntityTag.explosion_radius
        // @group properties
        // @description
        // If this entity can explode, returns its explosion radius.
        // -->
        if (attribute.startsWith("explosion_radius")) {
            return new ElementTag(getExplosionRadius())
                    .getObjectAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object EntityTag
        // @name explosion_radius
        // @input ElementTag(Decimal)
        // @description
        // If this entity can explode, sets its explosion radius.
        // @tags
        // <EntityTag.explosion_radius>
        // -->
        if (mechanism.matches("explosion_radius") && mechanism.requireFloat()) {
            if (entity.getBukkitEntity() instanceof Creeper) {
                try {
                    FieldUtils.writeField(entity.getBukkitEntityType().getClass(), "explosionRadius", mechanism.getValue().asInt());
                } catch (IllegalAccessException e) {
                    //todo debug msg
                }
            }
            else {
                //todo unavailable on 1.18
//                ((PrimedTnt) entity.getBukkitEntity()).setYield(mechanism.getValue().asFloat());
            }
        }
    }
}
