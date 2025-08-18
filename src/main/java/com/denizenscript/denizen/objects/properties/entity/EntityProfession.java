package com.denizenscript.denizen.objects.properties.entity;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;

public class EntityProfession implements Property {

    // TODO This technically has registries on all supported versions
    public static boolean describes(ObjectTag entity) {
        if (!(entity instanceof EntityTag)) {
            return false;
        }
        return ((EntityTag) entity).getBukkitEntityType() == EntityType.VILLAGER
                || ((EntityTag) entity).getBukkitEntityType() == EntityType.ZOMBIE_VILLAGER;
    }

    public static EntityProfession getFrom(ObjectTag entity) {
        if (!describes(entity)) {
            return null;
        }
        else {
            return new EntityProfession((EntityTag) entity);
        }
    }

    public static final String[] handledTags = new String[] {
            "profession"
    };

    public static final String[] handledMechs = new String[] {
            "profession"
    };

    public EntityProfession(EntityTag entity) {
        professional = entity;
    }

    EntityTag professional;

    public VillagerProfession getProfession() {
        return ((Villager)professional.getBukkitEntity()).getVillagerData().getProfession();
    }

    public void setProfession(VillagerProfession profession) {
        ((Villager)professional.getBukkitEntity()).getVillagerData().setProfession(profession);
    }

    @Override
    public String getPropertyString() {
        return CoreUtilities.toLowerCase(String.valueOf(getProfession()));
    }

    @Override
    public String getPropertyId() {
        return "profession";
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <EntityTag.profession>
        // @returns ElementTag
        // @mechanism EntityTag.profession
        // @group properties
        // @description
        // If the entity can have professions, returns the entity's profession.
        // Currently, only Villager-type and infected zombie entities can have professions.
        // For the list of possible professions, refer to <@link url https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/VillagerProfession.html>
        // -->
        if (attribute.startsWith("profession")) {
            return new ElementTag(String.valueOf(getProfession()), true)
                    .getObjectAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object EntityTag
        // @name profession
        // @input ElementTag
        // @description
        // Changes the entity's profession.
        // Currently, only Villager-type entities can have professions.
        // For the list of possible professions, refer to <@link url https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/VillagerProfession.html>
        // @tags
        // <EntityTag.profession>
        // -->
        if (mechanism.matches("profession") && Utilities.requireEnumlike(mechanism, VillagerProfession.class)) {
            setProfession(Utilities.elementToEnumlike(mechanism.getValue(), VillagerProfession.class));
        }
    }
}
