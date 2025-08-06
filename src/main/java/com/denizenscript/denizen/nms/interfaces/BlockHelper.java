package com.denizenscript.denizen.nms.interfaces;

import com.denizenscript.denizen.nms.util.PlayerProfile;
import com.denizenscript.denizen.nms.util.jnbt.CompoundTag;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.utilities.Location;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SkullBlock;

import java.util.Set;

public interface BlockHelper {


    void applyPhysics(Location location);

    PlayerProfile getPlayerProfile(SkullBlock skull);

    void setPlayerProfile(SkullBlock skull, PlayerProfile playerProfile);

    CompoundTag getNbtData(Block block);

    void setNbtData(Block block, CompoundTag compoundTag);

    boolean setBlockResistance(Block material, float resistance);

    float getBlockResistance(Block material);

    enum PistonPushReaction {
        NORMAL, DESTROY, BLOCK, IGNORE, PUSH_ONLY;
        public static final PistonPushReaction[] VALUES = values();
    }

    default PistonPushReaction getPushReaction(Block mat) { // TODO: once minimum version is 1.19, remove from NMS
        return PistonPushReaction.VALUES[mat.createBlockData().getPistonMoveReaction().ordinal()];
    }

    void setPushReaction(Block mat, PistonPushReaction reaction);

    float getBlockStrength(Block mat);

    void setBlockStrength(Block mat, float strength);

    static String getMaterialNameFromBlockData(String text) {
        int openBracket = text.indexOf('[');
        String material = text;
        if (openBracket > 0) {
            material = text.substring(0, openBracket);
        }
        if (material.startsWith("minecraft:")) {
            material = material.substring("minecraft:".length());
        }
        return material;
    }

    default BlockData parseBlockData(String text) {
        int openBracket = text.indexOf('[');
        String material = text;
        String otherData = null;
        if (openBracket > 0) {
            material = text.substring(0, openBracket);
            otherData = text.substring(openBracket);
        }
        return Material.matchMaterial(material).createBlockData(otherData);
    }

    default void makeBlockStateRaw(BlockState state) {} // TODO: once 1.19 is the minimum supported version, remove this

    void doRandomTick(Location location);

    Instrument getInstrumentFor(Block mat);

    default void ringBell(Bell bell) { /// TODO: once minimum version is 1.19, remove from NMS
        bell.ring();
    }

    int getExpDrop(Block block, ItemStack item);

    default void setSpawnerCustomRules(CreatureSpawner spawner, int skyMin, int skyMax, int blockMin, int blockMax) {
        throw new UnsupportedOperationException();
    }

    default void setSpawnerSpawnedType(CreatureSpawner spawner, EntityTag entity) {
        spawner.setSpawnedType(entity.getBukkitEntityType());
    }

    default Color getMapColor(Block block) { // TODO: once 1.20 is the minimum supported version, remove from NMS
        return block.getBlockData().getMapColor();
    }

    default void setVanillaTags(Material material, Set<NamespacedKey> tags) { // TODO: once 1.21 is the minimum supported version, remove this
        throw new UnsupportedOperationException();
    }
}
