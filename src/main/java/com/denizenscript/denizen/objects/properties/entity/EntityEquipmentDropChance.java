package com.denizenscript.denizen.objects.properties.entity;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.utilities.ReflectionHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Ref;

public class EntityEquipmentDropChance extends EntityProperty<MapTag> {

    // <--[property]
    // @object EntityTag
    // @name equipment_drop_chance
    // @input MapTag
    // @description
    // Controls the chance of each piece of equipment dropping when the entity dies.
    // A drop chance of 0 will prevent the item from dropping, a drop chance of 1 will always drop the item if killed by a player, and a drop chance of higher than 1 will always drop the item no matter what the entity was killed by.
    // A map of equipment slots to drop chances, with keys "head", "chest", "legs", "feet", "hand", and "off_hand".
    //
    // @tag-example
    // # Use to narrate the drop chances of a zombie's equipment:
    // - narrate <[zombie].equipment_drop_chance>
    //
    // @mechanism-example
    // # Use to prevent a zombie from dropping any of its equipped items, no matter what:
    // - adjust <[zombie]> equipment_drop_chance:[head=0;chest=0;legs=0;feet=0;hand=0;off_hand=0]
    //
    // -->

    public static boolean describes(EntityTag entity) {
        return entity.getBukkitEntity() instanceof Mob;
    }

    @Override
    public MapTag getPropertyValue() {
        MapTag map = new MapTag();
        float[] dropChanceByIndex = new float[6];
        Method method = ReflectionHelper.getMethod(getLivingEntity().getClass(), "getEquipmentDropChance", EquipmentSlot.class);
        if (method != null) {
            for (EquipmentSlot equipmentslot : EquipmentSlot.values()) {
                try {
                    int handIndexOffset = equipmentslot.getType().equals(EquipmentSlot.Type.HAND) ? 4 : 0;
                    dropChanceByIndex[equipmentslot.getIndex() + handIndexOffset] = (float) method.invoke(getLivingEntity(), equipmentslot);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    ReflectionHelper.echoError(e);
                }
            }
        }
        //todo get correct id for each
        map.putObject("head", new ElementTag(dropChanceByIndex[3]));
        map.putObject("chest", new ElementTag(dropChanceByIndex[2]));
        map.putObject("legs", new ElementTag(dropChanceByIndex[1]));
        map.putObject("feet", new ElementTag(dropChanceByIndex[0]));
        map.putObject("hand", new ElementTag(dropChanceByIndex[4]));
        map.putObject("off_hand", new ElementTag(dropChanceByIndex[5]));
        return map;
    }

    @Override
    public void setPropertyValue(MapTag map, Mechanism mechanism) {
        ElementTag head = map.getElement("head");
        ElementTag chest = map.getElement("chest");
        ElementTag legs = map.getElement("legs");
        ElementTag feet = map.getElement("feet");
        ElementTag hand = map.getElement("hand");
        ElementTag offHand = map.getElement("off_hand");
        if (head != null) {
            setDropChance(getLivingEntity(), EquipmentSlot.Type.ARMOR, 3, head.asFloat());
        }
        if (chest != null) {
            setDropChance(getLivingEntity(), EquipmentSlot.Type.ARMOR, 2, chest.asFloat());
        }
        if (legs != null) {
            setDropChance(getLivingEntity(), EquipmentSlot.Type.ARMOR, 1, legs.asFloat());
        }
        if (feet != null) {
            setDropChance(getLivingEntity(), EquipmentSlot.Type.ARMOR, 0, feet.asFloat());
        }
        if (hand != null) {
            setDropChance(getLivingEntity(), EquipmentSlot.Type.HAND, 0, hand.asFloat());
        }
        if (offHand != null) {
            setDropChance(getLivingEntity(), EquipmentSlot.Type.HAND, 1, offHand.asFloat());
        }
    }

    public void setDropChance(LivingEntity entity, EquipmentSlot.Type type, int index, float value) {
        switch (type) {
            case HAND:
                try {
                    Field field = FieldUtils.getField(entity.getClass(), "handDropChances");
                    ((float[]) field.get(entity))[index] = value;
                } catch (Throwable ex) {
                    ReflectionHelper.echoError(ex);
                }
                break;
            case ARMOR:
                try {
                    Field field = FieldUtils.getField(entity.getClass(), "armorDropChances");
                    ((float[]) field.get(entity))[index] = value;
                } catch (Throwable ex) {
                    ReflectionHelper.echoError(ex);
                }
                break;
        }
    }

    @Override
    public String getPropertyId() {
        return "equipment_drop_chance";
    }

    public static void register() {
        autoRegister("equipment_drop_chance", EntityEquipmentDropChance.class, MapTag.class, false);
    }
}
