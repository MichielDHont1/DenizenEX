package com.denizenscript.denizen.nms.interfaces;

import com.denizenscript.denizen.nms.NMSHandler;
import com.denizenscript.denizen.nms.NMSVersion;
import com.denizenscript.denizen.nms.util.PlayerProfile;
import com.denizenscript.denizen.nms.util.jnbt.CompoundTag;
import com.denizenscript.denizen.nms.util.jnbt.Tag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.utilities.NamespacedKey;
import com.denizenscript.denizen.utilities.nbt.CustomNBT;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.google.gson.JsonObject;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;


import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class ItemHelper {



    //todo materials
//    public abstract void setMaxStackSize(Material material, int size);
//
//    public abstract Integer burnTime(Material material);

    public abstract void registerStonecuttingRecipe(String keyName, String group, ItemStack result, ItemStack[] ingredient, boolean exact);

    public abstract void registerFurnaceRecipe(String keyName, String group, ItemStack result, ItemStack[] ingredient, float exp, int time, String type, boolean exact, String category);

    public abstract void registerShapelessRecipe(String keyName, String group, ItemStack result, List<ItemStack[]> ingredients, boolean[] exact, String category);
//todo recipe
//    public abstract void setShapedRecipeIngredient(ShapedRecipe recipe, char c, ItemStack[] item, boolean exact);

    public abstract String getJsonString(ItemStack itemStack);

//    public String getLegacyHoverNbt(ItemTag item) { // TODO: once 1.20 is the minimum supported version, remove this
//        return item.getItemMeta().getAsString();
//    }

    public JsonObject getRawHoverComponentsJson(ItemStack item) {
        throw new UnsupportedOperationException();
    }

    public ItemStack applyRawHoverComponentsJson(ItemStack item, JsonObject components) {
        throw new UnsupportedOperationException();
    }

    public abstract PlayerProfile getSkullSkin(ItemStack itemStack);

    public abstract ItemStack setSkullSkin(ItemStack itemStack, PlayerProfile playerProfile);

    public abstract ItemStack addNbtData(ItemStack itemStack, String key, Tag value);

    public abstract CompoundTag getNbtData(ItemStack itemStack);

    public abstract ItemStack setNbtData(ItemStack itemStack, CompoundTag compoundTag);

    public CompoundTag getCustomData(ItemStack item) { // TODO: once 1.20 is the minimum supported version, remove default impl
        return getNbtData(item);
    }

    public ItemStack setCustomData(ItemStack item, CompoundTag data) { // TODO: once 1.20 is the minimum supported version, remove default impl
        return setNbtData(item, data);
    }

    public ItemStack setPartialOldNbt(ItemStack item, CompoundTag oldTag) {
        throw new UnsupportedOperationException();
    }

    public CompoundTag getEntityData(ItemStack item) { // TODO: once 1.20 is the minimum supported version, remove default impl
        CompoundTag nbt = getNbtData(item);
        return nbt != null && nbt.getValue().get("EntityTag") instanceof CompoundTag entityNbt ? entityNbt : null;
    }

    public ItemStack setEntityData(ItemStack item, CompoundTag entityNbt, EntityType<?> entityType) { // TODO: once 1.20 is the minimum supported version, remove default impl
        boolean shouldRemove = entityNbt == null || entityNbt.isEmpty();
        CompoundTag nbt = getNbtData(item);
        if (shouldRemove && !nbt.containsKey("EntityTag")) {
            return item;
        }
        if (shouldRemove) {
            nbt = nbt.createBuilder().remove("EntityTag").build();
        }
        else {
            nbt = nbt.createBuilder().put("EntityTag", entityNbt).build();
        }
        return setNbtData(item, nbt);
    }
//todo materials
//    public List<Material> getCanPlaceOn(ItemStack item) { // TODO: once 1.20 is the minimum supported version, remove default impl
//        return CustomNBT.getNBTMaterials(item, CustomNBT.KEY_CAN_PLACE_ON);
//    }
//
//    public ItemStack setCanPlaceOn(ItemStack item, List<Material> canPlaceOn) { // TODO: once 1.20 is the minimum supported version, remove default impl
//        if (canPlaceOn == null) {
//            return CustomNBT.clearNBT(item, CustomNBT.KEY_CAN_PLACE_ON);
//        }
//        return CustomNBT.setNBTMaterials(item, CustomNBT.KEY_CAN_PLACE_ON, canPlaceOn);
//    }
//
//    public List<Material> getCanBreak(ItemStack item) { // TODO: once 1.20 is the minimum supported version, remove default impl
//        return CustomNBT.getNBTMaterials(item, CustomNBT.KEY_CAN_DESTROY);
//    }
//
//    public ItemStack setCanBreak(ItemStack item, List<Material> canBreak) { // TODO: once 1.20 is the minimum supported version, remove default impl
//        if (canBreak == null) {
//            return CustomNBT.clearNBT(item, CustomNBT.KEY_CAN_DESTROY);
//        }
//        return CustomNBT.setNBTMaterials(item, CustomNBT.KEY_CAN_DESTROY, canBreak);
//    }

    public MapTag getRawComponentsPatch(ItemStack item, boolean excludeHandled) {
        throw new UnsupportedOperationException();
    }

    public ItemStack setRawComponentsPatch(ItemStack item, MapTag rawComponentsMap, int dataVersion, Consumer<String> errorHandler) {
        throw new UnsupportedOperationException();
    }

    public abstract void registerSmithingRecipe(String keyName, ItemStack result, ItemStack[] baseItem, boolean baseExact, ItemStack[] upgradeItem, boolean upgradeExact, ItemStack[] templateItem, boolean templateExact);

    public abstract void setInventoryItem(Container inventory, ItemStack item, int slot);

    public abstract String getDisplayName(ItemTag item);

    public abstract List<String> getLore(ItemTag item);

    public abstract void setDisplayName(ItemTag item, String name);

    public abstract void setLore(ItemTag item, List<String> lore);

    public boolean renderEntireMap(int mapId, int xMin, int zMin, int xMax, int zMax) {
        throw new UnsupportedOperationException();
    }

    public Block getPlacedBlock(Item material) {
        throw new UnsupportedOperationException();
    }

    public abstract boolean isValidMix(ItemStack input, ItemStack ingredient);

    //todo recipes
//    public record BrewingRecipe(RecipeChoice input, RecipeChoice ingredient, ItemStack result) {}
//
//    public Map<NamespacedKey, BrewingRecipe> getCustomBrewingRecipes() {
//        throw new UnsupportedOperationException();
//    }

//    public byte[] renderMap(MapView mapView, Player player) {
//        throw new UnsupportedOperationException();
//    }

//    public int getFoodPoints(Material itemType) {
//        throw new UnsupportedOperationException();
//    }

//    public DyeColor getShieldColor(ItemStack item) { // TODO: once 1.21 is the minimum supported version, remove from NMS
//        if (NMSHandler.getVersion().isAtLeast(NMSVersion.v1_21)) {
//            return ((ShieldMeta) item.getItemMeta()).getBaseColor();
//        }
//        // TODO: once 1.20 is the minimum supported version, remove legacy code ↓
//        BlockStateMeta stateMeta = (BlockStateMeta) item.getItemMeta();
//        return stateMeta.hasBlockState() ? ((Banner) stateMeta.getBlockState()).getBaseColor() : null;
//    }

//    public ItemStack setShieldColor(ItemStack item, DyeColor color) { // TODO: once 1.21 is the minimum supported version, remove from NMS
//        if (NMSHandler.getVersion().isAtLeast(NMSVersion.v1_21)) {
//            ShieldMeta shieldMeta = (ShieldMeta) item.getItemMeta();
//            shieldMeta.setBaseColor(color);
//            item.setItemMeta(shieldMeta);
//            return item;
//        }
//        // TODO: once 1.20 is the minimum supported version, remove legacy code ↓
//        if (color == null) {
//            CompoundTag noStateNbt = getNbtData(item).createBuilder().remove("BlockEntityTag").build();
//            return setNbtData(item, noStateNbt);
//        }
//        BlockStateMeta stateMeta = (BlockStateMeta) item.getItemMeta();
//        Banner banner = (Banner) stateMeta.getBlockState();
//        banner.setBaseColor(color);
//        stateMeta.setBlockState(banner);
//        item.setItemMeta(stateMeta);
//        return item;
//    }

    public void blockRecipeFinalization() {
    }

    public void restoreRecipeFinalization() {
    }

//    public void removeRecipes(List<NamespacedKey> keys) {
//    }
//todo recipes
//    public void registerOtherRecipe(org.bukkit.inventory.Recipe recipe) {
//    }
}
