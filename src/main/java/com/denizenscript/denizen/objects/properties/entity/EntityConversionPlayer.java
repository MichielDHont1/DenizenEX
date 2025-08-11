package com.denizenscript.denizen.objects.properties.entity;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizen.nms.abstracts.OfflinePlayer;
import com.denizenscript.denizencore.utilities.ReflectionHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.ZombieVillager;

import java.util.UUID;

public class EntityConversionPlayer implements Property {

    public static boolean describes(ObjectTag object) {
        if (!(object instanceof EntityTag)) {
            return false;
        }
        Entity entity = ((EntityTag) object).getBukkitEntity();
        return entity instanceof ZombieVillager;
    }

    public static EntityConversionPlayer getFrom(ObjectTag entity) {
        if (!describes(entity)) {
            return null;
        }
        else {
            return new EntityConversionPlayer((EntityTag) entity);
        }
    }

    public static final String[] handledMechs = new String[] {
            "conversion_player"
    };

    public EntityConversionPlayer(EntityTag ent) {
        entity = ent;
    }

    EntityTag entity;

    @Override
    public String getPropertyString() {
        UUID uuid = ReflectionHelper.getFieldValue(getZombieVillager().getClass(), "conversionStarter", getZombieVillager());
        OfflinePlayer player = new OfflinePlayer(uuid);
        if (player.uuid != null && player.hasPlayedBefore()) {
            return new PlayerTag(player).identify();
        }
        return null;
    }

    @Override
    public String getPropertyId() {
        return "conversion_player";
    }

    public static void register() {

        // <--[tag]
        // @attribute <EntityTag.conversion_player>
        // @returns PlayerTag
        // @mechanism EntityTag.conversion_player
        // @group properties
        // @description
        // Returns the player that caused a zombie villager to start converting back to a villager, if any.
        // -->
        PropertyParser.registerTag(EntityConversionPlayer.class, PlayerTag.class, "conversion_player", (attribute, object) -> {
            UUID uuid = ReflectionHelper.getFieldValue(object.getZombieVillager().getClass(), "conversionStarter", object.getZombieVillager());
            OfflinePlayer player = new OfflinePlayer(uuid);
            if (player.uuid != null && player.hasPlayedBefore()) {
                return new PlayerTag(player);
            }
            return null;
        });
    }

    public ZombieVillager getZombieVillager() {
        return (ZombieVillager) entity.getBukkitEntity();
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object EntityTag
        // @name conversion_player
        // @input PlayerTag
        // @description
        // Sets the player that caused a zombie villager to start converting back to a villager.
        // Give no input to remove the player ID from the zombie-villager.
        // @tags
        // <EntityTag.conversion_player>
        // -->
        if (mechanism.matches("conversion_player")) {
            if (mechanism.hasValue()) {
                if (mechanism.requireObject(PlayerTag.class)) {
                    ReflectionHelper.setFieldValue(getZombieVillager().getClass(), "conversionStarter", getZombieVillager(), mechanism.valueAsType(PlayerTag.class).getUUID());
                }
            }
            else {
                ReflectionHelper.setFieldValue(getZombieVillager().getClass(), "conversionStarter", getZombieVillager(), null);
            }
        }
    }
}
