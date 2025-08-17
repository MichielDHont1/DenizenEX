package com.denizenscript.denizen.objects.properties.item;

import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;

public class ItemPatterns implements Property {

    public static boolean isBannerOrShield(ItemStack itemStack) {
        Item banneritem = itemStack.getItem();
        return banneritem == Items.SHIELD || banneritem.getName(itemStack).getString().endsWith("_BANNER");
    }

    public static boolean describes(ObjectTag item) {
        if (item instanceof ItemTag) {
            ItemStack material = ((ItemTag) item).getItemStack();
            return isBannerOrShield(material);
        }
        return false;
    }

    public static ItemPatterns getFrom(ObjectTag item) {
        if (!describes(item)) {
            return null;
        }
        else {
            return new ItemPatterns((ItemTag) item);
        }
    }

    public static final String[] handledTags = new String[] {
            "patterns"
    };

    public static final String[] handledMechs = new String[] {
            "patterns"
    };

    public ItemPatterns(ItemTag item) {
        this.item = item;
    }

    ItemTag item;

    public ListTag listPatterns() {
        ListTag list = new ListTag();
        net.minecraft.nbt.ListTag listtag = getPatterns();
        for(int i = 0; i < listtag.size() && i < 6; ++i) {
            CompoundTag compoundtag1 = listtag.getCompound(i);
            DyeColor dyecolor = DyeColor.byId(compoundtag1.getInt("Color"));
            BannerPattern bannerpattern = BannerPattern.byHash(compoundtag1.getString("Pattern"));
            if (bannerpattern != null)
            {
                list.add(dyecolor.name() + "/" + bannerpattern.name());
            }
            else
            {
                list.add(dyecolor.name() + "/NULL" );
            }
        }
        return list;
    }

    public net.minecraft.nbt.ListTag getPatterns() {
        CompoundTag blockEntityData = BlockItem.getBlockEntityData(item.getItemStack());
        if (blockEntityData != null && blockEntityData.contains("Patterns")) {
            return blockEntityData.getList("Patterns", 10);
        } else {
            return null;
        }
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <ItemTag.patterns>
        // @returns ListTag
        // @group properties
        // @mechanism ItemTag.patterns
        // @description
        // Lists a banner's patterns in the form "COLOR/PATTERN|COLOR/PATTERN" etc.
        // For the list of possible colors, see <@link url https://hub.spigotmc.org/javadocs/spigot/org/bukkit/DyeColor.html>.
        // For the list of possible patterns, see <@link url https://hub.spigotmc.org/javadocs/spigot/org/bukkit/block/banner/PatternType.html>.
        // -->
        if (attribute.startsWith("patterns")) {
            return listPatterns().getObjectAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public String getPropertyString() {
        ListTag list = listPatterns();
        if (list.isEmpty()) {
            return null;
        }
        return list.identify();
    }

    @Override
    public String getPropertyId() {
        return "patterns";
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object ItemTag
        // @name patterns
        // @input ListTag
        // @description
        // Changes the patterns of a banner. Input must be in the form
        // "COLOR/PATTERN|COLOR/PATTERN" etc.
        // For the list of possible colors, see <@link url https://hub.spigotmc.org/javadocs/spigot/org/bukkit/DyeColor.html>.
        // For the list of possible patterns, see <@link url https://hub.spigotmc.org/javadocs/spigot/org/bukkit/block/banner/PatternType.html>.
        // @tags
        // <ItemTag.patterns>
        // <server.pattern_types>
        // -->
        if (mechanism.matches("patterns")) {
            CompoundTag compoundtag = BlockItem.getBlockEntityData(item.getItemStack());
            net.minecraft.nbt.ListTag patterns = new net.minecraft.nbt.ListTag();
            ListTag list = mechanism.valueAsType(ListTag.class);
            List<String> split;
            for (String string : list) {
                try {
                    CompoundTag Pattern = new CompoundTag();
                    split = CoreUtilities.split(string, '/', 2);
                    Pattern.putInt("Color", DyeColor.valueOf(split.get(0).toUpperCase()).getId());
                    BannerPattern bannerPattern = BannerPattern.byFilename(split.get(1).toUpperCase());
                    if (bannerPattern != null)
                    {
                        Pattern.putString("Pattern",bannerPattern .getHashname());
                    }
                    patterns.add(Pattern);
                }
                catch (Exception e) {
                    Debug.echoError("Could not apply pattern to banner: " + string);
                }
            }
            if (compoundtag != null)
            {
                compoundtag.put("Patterns", patterns);
                item.getItemStack().addTagElement("BlockEntityTag", compoundtag);
            }
        }
    }
}
