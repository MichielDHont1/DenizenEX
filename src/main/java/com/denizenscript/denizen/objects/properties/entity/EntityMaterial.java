package com.denizenscript.denizen.objects.properties.entity;

import com.denizenscript.denizen.nms.NMSHandler;
import com.denizenscript.denizen.nms.NMSVersion;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.MaterialTag;
import com.denizenscript.denizencore.objects.Mechanism;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class EntityMaterial extends EntityProperty<MaterialTag> {

    // <--[property]
    // @object EntityTag
    // @name material
    // @input MaterialTag
    // @description
    // An entity's associated block material.
    // For endermen, this is the block being held.
    // For minecarts, this is the block being carried.
    // For block displays, this is the block being displayed.
    // -->

    public static boolean describes(EntityTag entity) {
        return entity.getBukkitEntity() instanceof EnderMan
                || entity.getBukkitEntity() instanceof Minecart; //todo added in 1.19
        /*      || (NMSHandler.getVersion().isAtLeast(NMSVersion.v1_19) && entity.getBukkitEntity() instanceof BlockDisplay);*/
    }

    @Override
    public MaterialTag getPropertyValue() {
        BlockState blockData = null;
        if (getEntity() instanceof EnderMan enderman) {
            blockData = enderman.getCarriedBlock();
        }
        else if (getEntity() instanceof Minecart minecart) {
            blockData = minecart.getDisplayBlockState();
        }
        //todo 1.19
//        else if (NMSHandler.getVersion().isAtLeast(NMSVersion.v1_19)) {
//            blockData = as(BlockDisplay.class).getBlock();
//        }
        return blockData != null ? new MaterialTag(blockData) : new MaterialTag(Blocks.AIR.defaultBlockState());
    }

    @Override
    public boolean isDefaultValue(MaterialTag value) {
        return value.getBlock() == Blocks.AIR;
    }

    @Override
    public void setPropertyValue(MaterialTag value, Mechanism mechanism) {
        if (getEntity() instanceof EnderMan enderman) {
            enderman.setCarriedBlock(value.getBlock().defaultBlockState());
        }
        else if (getEntity() instanceof Minecart minecart) {
            minecart.setDisplayBlockState(value.getBlock().defaultBlockState());
        }
        //todo 1.19
//        else if (NMSHandler.getVersion().isAtLeast(NMSVersion.v1_19)) {
//            as(BlockDisplay.class).setBlock(value.getModernData());
//        }
    }

    @Override
    public String getPropertyId() {
        return "material";
    }

    public static void register() {
        autoRegister("material", EntityMaterial.class, MaterialTag.class, false);
    }
}
