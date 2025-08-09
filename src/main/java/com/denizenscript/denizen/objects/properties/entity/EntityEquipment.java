package com.denizenscript.denizen.objects.properties.entity;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.InventoryTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizen.utilities.BukkitImplDeprecations;
//import net.citizensnpcs.api.trait.trait.Equipment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;

public class EntityEquipment implements Property {

    public static boolean describes(ObjectTag entity) {
        return entity instanceof EntityTag
                && ((EntityTag) entity).isLivingEntity();
    }

    public static EntityEquipment getFrom(ObjectTag entity) {
        if (!describes(entity)) {
            return null;
        }
        else {
            return new EntityEquipment((EntityTag) entity);
        }
    }

    public static final String[] handledMechs = new String[] {
            "equipment"
    };

    public EntityEquipment(EntityTag ent) {
        entity = ent;
    }

    EntityTag entity;

    @Override
    public String getPropertyString() {
        return entity.getEquipment().identify();
    }

    @Override
    public String getPropertyId() {
        return "equipment";
    }

    public static void register() {

        // <--[tag]
        // @attribute <EntityTag.equipment>
        // @returns ListTag(ItemTag)
        // @mechanism EntityTag.equipment
        // @group inventory
        // @description
        // Returns a ListTag containing the entity's equipment.
        // Output list is boots|leggings|chestplate|helmet
        // -->
        PropertyParser.registerTag(EntityEquipment.class, ObjectTag.class, "equipment", (attribute, object) -> {
            ArrayList<ItemStack> equipment = new ArrayList<ItemStack>() ;
            object.entity.getLivingEntity().getArmorSlots().forEach(equipment::add);
            if (attribute.startsWith("equipment.boots")) {
                BukkitImplDeprecations.entityEquipmentSubtags.warn(attribute.context);
                attribute.fulfill(1);
                ItemStack boots = equipment.get(0);
                return new ItemTag(boots != null ? boots : new ItemStack(Items.AIR));
            }
            else if (attribute.startsWith("equipment.chestplate") || attribute.startsWith("equipment.chest")) {
                BukkitImplDeprecations.entityEquipmentSubtags.warn(attribute.context);
                attribute.fulfill(1);
                ItemStack chestplate = equipment.get(2);
                return new ItemTag(chestplate != null ? chestplate : new ItemStack(Items.AIR));
            }
            else if (attribute.startsWith("equipment.helmet") || attribute.startsWith("equipment.head")) {
                BukkitImplDeprecations.entityEquipmentSubtags.warn(attribute.context);
                attribute.fulfill(1);
                ItemStack helmet = equipment.get(3);
                return new ItemTag(helmet != null ? helmet : new ItemStack(Items.AIR));
            }
            else if (attribute.startsWith("equipment.leggings") || attribute.startsWith("equipment.legs")) {
                BukkitImplDeprecations.entityEquipmentSubtags.warn(attribute.context);
                attribute.fulfill(1);
                ItemStack leggings = equipment.get(1);
                return new ItemTag(leggings != null ? leggings : new ItemStack(Items.AIR));
            }
            return object.entity.getEquipment();
        });

        // <--[tag]
        // @attribute <EntityTag.equipment_map>
        // @returns MapTag
        // @mechanism EntityTag.equipment
        // @group inventory
        // @description
        // Returns a MapTag containing the entity's equipment.
        // Output keys are boots, leggings, chestplate, helmet.
        // Air items will be left out of the map.
        // -->
        PropertyParser.registerTag(EntityEquipment.class, MapTag.class, "equipment_map", (attribute, object) -> {
            MapTag output = new MapTag();
            ArrayList<ItemStack> equipment = new ArrayList<ItemStack>() ;
            object.entity.getLivingEntity().getArmorSlots().forEach(equipment::add);
            InventoryTag.addToMapIfNonAir(output, "boots", equipment.get(0));
            InventoryTag.addToMapIfNonAir(output, "leggings", equipment.get(1));
            InventoryTag.addToMapIfNonAir(output, "chestplate", equipment.get(2));
            InventoryTag.addToMapIfNonAir(output, "helmet", equipment.get(3));
            return output;
        });
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object EntityTag
        // @name equipment
        // @input MapTag
        // @description
        // Sets the entity's worn equipment.
        // Input keys are boots, leggings, chestplate, and helmet.
        // @tags
        // <EntityTag.equipment>
        // <EntityTag.equipment_map>
        // -->
        if (mechanism.matches("equipment") && mechanism.hasValue()) {
            ArrayList<ItemStack> equipment = new ArrayList<ItemStack>() ;
            entity.getLivingEntity().getArmorSlots().forEach(equipment::add);
            if (mechanism.value.canBeType(MapTag.class)) {
                MapTag map = mechanism.valueAsType(MapTag.class);
                ItemTag boots = map.getObjectAs("boots", ItemTag.class, mechanism.context);
                if (boots != null) {
                    ItemStack bootsItem = boots.getItemStack();
//                    if (entity.isCitizensNPC()) {
//                        entity.getDenizenNPC().getEquipmentTrait().set(Equipment.EquipmentSlot.BOOTS, bootsItem);
//                    }
//                    else {
                        equipment.set(0,bootsItem);
//                    }
                }
                ItemTag leggings = map.getObjectAs("leggings", ItemTag.class, mechanism.context);
                if (leggings != null) {
                    ItemStack leggingsItem = leggings.getItemStack();
//                    if (entity.isCitizensNPC()) {
//                        entity.getDenizenNPC().getEquipmentTrait().set(Equipment.EquipmentSlot.LEGGINGS, leggingsItem);
//                    }
//                    else {
                        equipment.set(1, leggingsItem);
//                    }
                }
                ItemTag chestplate = map.getObjectAs("chestplate", ItemTag.class, mechanism.context);
                if (chestplate != null) {
                    ItemStack chestplateItem = chestplate.getItemStack();
//                    if (entity.isCitizensNPC()) {
//                        entity.getDenizenNPC().getEquipmentTrait().set(Equipment.EquipmentSlot.CHESTPLATE, chestplateItem);
//                    }
//                    else {
                        equipment.set(2, chestplateItem);
//                    }
                }
                ItemTag helmet = map.getObjectAs("helmet", ItemTag.class, mechanism.context);
                if (helmet != null) {
                    ItemStack helmetItem = helmet.getItemStack();
//                    if (entity.isCitizensNPC()) {
//                        entity.getDenizenNPC().getEquipmentTrait().set(Equipment.EquipmentSlot.HELMET, helmetItem);
//                    }
//                    else {
                        equipment.set(3, helmetItem);
//                    }
                }
            }
            else { // Soft-deprecate: no warn, but long term back-support
                ListTag list = mechanism.valueAsType(ListTag.class);
                ItemStack[] stacks = new ItemStack[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    stacks[i] = ItemTag.valueOf(list.get(i), mechanism.context).getItemStack();
                }
                //todo
//                entity.setArmorContents(stacks);
            }
        }
    }
}
