package com.denizenscript.denizen.objects.properties.entity;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class EntityFirework implements Property {

    public static boolean describes(ObjectTag entity) {
        return entity instanceof EntityTag entityTag && entityTag.getBukkitEntity() instanceof FireworkRocketEntity;
    }

    public static EntityFirework getFrom(ObjectTag entity) {
        if (!describes(entity)) {
            return null;
        }
        else {
            return new EntityFirework((EntityTag) entity);
        }
    }

    public static final String[] handledTags = new String[] {
            "firework_item"
    };

    public static final String[] handledMechs = new String[] {
            "firework_item"
    };

    public EntityFirework(EntityTag entity) {
        firework = entity;
    }

    EntityTag firework;

    @Override
    public String getPropertyString() {
        ItemStack item = new ItemStack(Items.FIREWORK_ROCKET);
        //todo metadata
//        item.setItemMeta(((Firework) firework.getBukkitEntity()).getFireworkMeta());
        return new ItemTag(item).identify();
    }

    @Override
    public String getPropertyId() {
        return "firework_item";
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <EntityTag.firework_item>
        // @returns ItemTag
        // @mechanism EntityTag.firework_item
        // @group properties
        // @description
        // If the entity is a firework, returns the firework item used to launch it.
        // -->
        if (attribute.startsWith("firework_item")) {
            ItemStack item = new ItemStack(Items.FIREWORK_ROCKET);
            //todo metadata
//            item.setItemMeta(((Firework) firework.getBukkitEntity()).getFireworkMeta());
            return new ItemTag(item).getObjectAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object EntityTag
        // @name firework_item
        // @input ItemTag
        // @description
        // Changes the firework effect on this entity, using a firework item.
        // @tags
        // <EntityTag.firework_item>
        // -->
        if (mechanism.matches("firework_item") && mechanism.requireObject(ItemTag.class)) {
            ItemTag item = mechanism.valueAsType(ItemTag.class);
            //todo metadata
//            if (item != null && item.getItemMeta() instanceof FireworkMeta) {
//                ((Firework) firework.getBukkitEntity()).setFireworkMeta((FireworkMeta) item.getItemMeta());
//            }
//            else {
                mechanism.echoError("'" + mechanism.getValue().asString() + "' is not a valid firework item.");
//            }
        }
    }
}
