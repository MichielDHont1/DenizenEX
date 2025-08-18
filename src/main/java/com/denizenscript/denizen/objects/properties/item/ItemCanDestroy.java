package com.denizenscript.denizen.objects.properties.item;

import com.denizenscript.denizen.nms.NMSHandler;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.objects.MaterialTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class ItemCanDestroy implements Property {

    public static boolean describes(ObjectTag item) {
        return item instanceof ItemTag;
    }

    public static ItemCanDestroy getFrom(ObjectTag item) {
        if (!describes(item)) {
            return null;
        }
        else {
            return new ItemCanDestroy((ItemTag) item);
        }
    }

    public static final String[] handledTags = new String[] {
            "can_destroy"
    };

    public static final String[] handledMechs = new String[] {
            "can_destroy"
    };

    public ItemCanDestroy(ItemTag item) {
        this.item = item;
    }

    ItemTag item;

    public ListTag getMaterials() {
        ItemStack itemStack = item.getItemStack();
        List<Block> materials = NMSHandler.itemHelper.getCanBreak(itemStack);
        if (materials != null && !materials.isEmpty()) {
            ListTag list = new ListTag();
            for (Block material : materials) {
                list.addObject(new MaterialTag(material));
            }
            return list;
        }
        return null;
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <ItemTag.can_destroy>
        // @returns ListTag(MaterialTag)
        // @group properties
        // @mechanism ItemTag.can_destroy
        // @description
        // Returns a list of materials this item can destroy while in adventure mode, if any.
        // -->
        if (attribute.startsWith("can_destroy")) {
            ListTag materials = getMaterials();
            if (materials != null) {
                return materials.getObjectAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }

    @Override
    public String getPropertyString() {
        ListTag materials = getMaterials();
        return materials != null ? materials.identify() : null;
    }

    @Override
    public String getPropertyId() {
        return "can_destroy";
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object ItemTag
        // @name can_destroy
        // @input ListTag(MaterialTag)
        // @description
        // Sets the materials this item can destroy while in adventure mode.
        // Leave empty to remove this property.
        // @tags
        // <ItemTag.can_destroy>
        // -->
        if (mechanism.matches("can_destroy")) {
            if (item.getItem() == Items.AIR) {
                mechanism.echoError("Cannot apply NBT to AIR!");
                return;
            }

            ItemStack itemStack = item.getItemStack();

            if (mechanism.hasValue()) {
                List<Block> materials = mechanism.valueAsType(ListTag.class).filter(Block, mechanism.context)
                        .stream().map(MaterialTag::getBlock).collect(Collectors.toList());
                itemStack = NMSHandler.itemHelper.setCanBreak(itemStack, materials);
            }
            else {
                itemStack = NMSHandler.itemHelper.setCanBreak(itemStack, null);
            }

            item.setItemStack(itemStack);
        }
    }
}
