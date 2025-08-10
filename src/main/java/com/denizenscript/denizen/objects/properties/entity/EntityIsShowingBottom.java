package com.denizenscript.denizen.objects.properties.entity;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;

public class EntityIsShowingBottom implements Property {

    public static boolean describes(ObjectTag entity) {
        return entity instanceof EntityTag entityTag && entityTag.getBukkitEntity() instanceof EndCrystal;
    }

    public static EntityIsShowingBottom getFrom(ObjectTag entity) {
        if (!describes(entity)) {
            return null;
        }
        else {
            return new EntityIsShowingBottom((EntityTag) entity);
        }
    }

    public static final String[] handledTags = new String[] {
            "is_showing_bottom"
    };

    public static final String[] handledMechs = new String[] {
            "is_showing_bottom"
    };

    public EntityIsShowingBottom(EntityTag entity) {
        dentity = entity;
    }

    EntityTag dentity;

    @Override
    public String getPropertyString() {
        if (((EndCrystal) dentity.getBukkitEntity()).showsBottom()) {
            return null;
        }
        else {
            return "false";
        }
    }

    @Override
    public String getPropertyId() {
        return "is_showing_bottom";
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <EntityTag.is_showing_bottom>
        // @returns ElementTag(Boolean)
        // @mechanism EntityTag.is_showing_bottom
        // @group properties
        // @description
        // If the entity is an ender crystal, returns whether the ender crystal has its bottom showing.
        // -->
        if (attribute.startsWith("is_showing_bottom")) {
            return new ElementTag(((EndCrystal) dentity.getBukkitEntity()).showsBottom())
                    .getObjectAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object EntityTag
        // @name is_showing_bottom
        // @input ElementTag(Boolean)
        // @description
        // Changes the bottom state of an ender crystal.
        // @tags
        // <EntityTag.is_showing_bottom>
        // -->
        if (mechanism.matches("is_showing_bottom") && mechanism.requireBoolean()) {
            ((EndCrystal) dentity.getBukkitEntity()).setShowBottom(mechanism.getValue().asBoolean());
        }
    }
}
