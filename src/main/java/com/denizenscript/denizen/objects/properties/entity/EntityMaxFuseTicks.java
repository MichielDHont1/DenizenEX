package com.denizenscript.denizen.objects.properties.entity;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import org.apache.commons.lang3.reflect.FieldUtils;

public class EntityMaxFuseTicks implements Property {

    public static boolean describes(ObjectTag object) {
        return object instanceof EntityTag && ((EntityTag) object).getBukkitEntityType() == EntityType.CREEPER;
    }

    public static EntityMaxFuseTicks getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new EntityMaxFuseTicks((EntityTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "max_fuse_ticks"
    };

    public static final String[] handledMechs = new String[] {
            "max_fuse_ticks"
    };

    public EntityMaxFuseTicks(EntityTag entity) {
        this.entity = entity;
    }

    EntityTag entity;

    @Override
    public String getPropertyString() {
        try
        {
            int maxFuse = (int)FieldUtils.readField((Object)entity, "maxSwell", true);
            return String.valueOf(maxFuse);
        } catch (IllegalAccessException e) {
            //todo debug msg
        }
        return null;
    }

    @Override
    public String getPropertyId() {
        return "max_fuse_ticks";
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <EntityTag.max_fuse_ticks>
        // @returns ElementTag(Number)
        // @mechanism EntityTag.max_fuse_ticks
        // @group properties
        // @description
        // Returns the default number of ticks until the creeper explodes when primed (NOT the time remaining if already primed).
        // -->
        if (attribute.startsWith("max_fuse_ticks")) {
            try
            {
                int maxFuse = (int)FieldUtils.readField((Object)entity, "maxSwell", true);
                return new ElementTag(maxFuse)
                        .getObjectAttribute(attribute.fulfill(1));
            } catch (IllegalAccessException e) {
                //todo debug msg
            }
            return null;
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object EntityTag
        // @name max_fuse_ticks
        // @input ElementTag(Number)
        // @description
        // Sets the default number of ticks until the creeper explodes when primed (NOT the time remaining if already primed).
        // @tags
        // <EntityTag.max_fuse_ticks>
        // -->
        if (mechanism.matches("max_fuse_ticks") && mechanism.requireInteger()) {
            try
            {
                FieldUtils.writeField((Object)entity, "maxSwell", (Object) mechanism.getValue().asInt());
            } catch (IllegalAccessException e) {
                //todo debug msg
            }
        }

    }
}
