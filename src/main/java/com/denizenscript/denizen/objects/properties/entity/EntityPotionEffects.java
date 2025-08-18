package com.denizenscript.denizen.objects.properties.entity;

import com.denizenscript.denizen.objects.properties.item.ItemPotion;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizen.utilities.BukkitImplDeprecations;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.projectile.Arrow;

import java.util.ArrayList;
import java.util.Collection;

public class EntityPotionEffects implements Property {

    public static boolean describes(ObjectTag object) {
        if (!(object instanceof EntityTag entity)) {
            return false;
        }
        return entity.isLivingEntity()
                || entity.getBukkitEntity() instanceof Arrow
                || entity.getBukkitEntity() instanceof AreaEffectCloud;
    }

    public static EntityPotionEffects getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new EntityPotionEffects((EntityTag) object);
        }
    }

    public static final String[] handledMechs = new String[] {
            "potion_effects"
    };

    public EntityPotionEffects(EntityTag entity) {
        this.entity = entity;
    }

    EntityTag entity;

    public Collection<PotionEffect> getEffectsList() {
        if (entity.isLivingEntity()) {
            return entity.getLivingEntity().getActivePotionEffects();
        }
        else if (isArrow()) {
            return getArrow().getCustomEffects();
        }
        else {
            return getAreaEffectCloud().getCustomEffects();
        }
    }

    public ListTag getEffectsListTag(TagContext context) {
        ListTag result = new ListTag();
        for (PotionEffect effect : getEffectsList()) {
            result.add(ItemPotion.effectToLegacyString(effect, context));
        }
        return result;
    }

    public ListTag getEffectsMapTag(boolean includeDeprecated) {
        ListTag result = new ListTag();
        for (PotionEffect effect : getEffectsList()) {
            result.addObject(ItemPotion.effectToMap(effect, includeDeprecated));
        }
        return result;
    }

    public boolean isArrow() {
        return entity.getBukkitEntity() instanceof Arrow;
    }

    public Arrow getArrow() {
        return (Arrow) entity.getBukkitEntity();
    }

    public AreaEffectCloud getAreaEffectCloud() {
        return (AreaEffectCloud) entity.getBukkitEntity();
    }

    public String getPropertyString() {
        ListTag effects = getEffectsMapTag(false);
        return effects.isEmpty() ? null : effects.identify();
    }

    @Override
    public String getPropertyId() {
        return "potion_effects";
    }

    public static void register() {

        // <--[tag]
        // @attribute <EntityTag.list_effects>
        // @returns ListTag
        // @group attribute
        // @mechanism EntityTag.potion_effects
        // @deprecated use 'effects_data' instead
        // @description
        // Deprecated in favor of <@link tag EntityTag.effects_data>
        // -->
        PropertyParser.registerTag(EntityPotionEffects.class, ListTag.class, "list_effects", (attribute, object) -> {
            BukkitImplDeprecations.oldPotionEffects.warn(attribute.context);
            return object.getEffectsListTag(attribute.context);
        });

        // <--[tag]
        // @attribute <EntityTag.effects_data>
        // @returns ListTag(MapTag)
        // @group attribute
        // @mechanism EntityTag.potion_effects
        // @description
        // Returns the active potion effects on the entity, or the potion effects an arrow/area effect cloud will apply.
        // The effects returned are a list of maps in <@link language Potion Effect Format>.
        // -->
        PropertyParser.registerTag(EntityPotionEffects.class, ListTag.class, "effects_data", (attribute, object) -> {
            return object.getEffectsMapTag(true);
        });

        // <--[tag]
        // @attribute <EntityTag.has_effect[<effect>]>
        // @returns ElementTag(Boolean)
        // @group attributes
        // @mechanism EntityTag.potion_effects
        // @description
        // Returns whether the entity has a specified effect, or whether an arrow/area effect cloud will apply a certain effect.
        // If no effect is specified, returns whether the entity has any effect.
        // The effect type must be from <@link url https://hub.spigotmc.org/javadocs/spigot/org/bukkit/potion/PotionEffectType.html>.
        // -->
        PropertyParser.registerTag(EntityPotionEffects.class, ElementTag.class, "has_effect", (attribute, object) -> {
            boolean returnElement = false;
            if (attribute.hasParam()) {
                PotionEffectType effectType = PotionEffectType.getByName(attribute.getParam());
                if (effectType == null) {
                    attribute.echoError("Invalid effect type specified: " + attribute.getParam());
                    return null;
                }
                if (object.entity.isLivingEntity()) {
                    returnElement = object.entity.getLivingEntity().hasPotionEffect(effectType);
                }
                else if (object.isArrow()) {
                    returnElement = object.getArrow().hasCustomEffect(effectType);
                }
                else {
                    returnElement = object.getAreaEffectCloud().hasCustomEffect(effectType);
                }
            }
            else if (!object.getEffectsList().isEmpty()) {
                returnElement = true;
            }
            return new ElementTag(returnElement);
        });
    }

    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object EntityTag
        // @name potion_effects
        // @input ListTag
        // @description
        // Set the entity's active potion effects, or the potion effects an arrow/area effect cloud will apply.
        // Each item in the list must be a MapTag in <@link language Potion Effect Format>.
        // @tags
        // <EntityTag.effects_data>
        // <EntityTag.has_effect[<effect>]>
        // -->
        if (mechanism.matches("potion_effects")) {
            for (ObjectTag effectObj : CoreUtilities.objectToList(mechanism.value, mechanism.context)) {
                PotionEffect effect;
                if (effectObj.canBeType(MapTag.class)) {
                    MapTag effectMap = effectObj.asType(MapTag.class, mechanism.context);
                    effect = ItemPotion.parseEffect(effectMap, mechanism.context);
                }
                else {
                    String effectStr = effectObj.toString();
                    effect = ItemPotion.parseLegacyEffectString(effectStr, mechanism.context);
                }
                if (effect == null) {
                    mechanism.echoError("Invalid potion effect '" + effectObj + "'");
                    continue;
                }
                if (entity.isLivingEntity()) {
                    entity.getLivingEntity().addPotionEffect(effect);
                }
                else if (isArrow()) {
                    getArrow().addCustomEffect(effect, true);
                }
                else {
                    getAreaEffectCloud().addCustomEffect(effect, true);
                }
            }
        }
    }
}
