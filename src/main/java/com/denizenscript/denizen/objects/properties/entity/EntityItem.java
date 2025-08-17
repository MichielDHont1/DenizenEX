package com.denizenscript.denizen.objects.properties.entity;

import com.denizenscript.denizen.nms.NMSHandler;
import com.denizenscript.denizen.nms.NMSVersion;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.utilities.BukkitImplDeprecations;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.TagContext;
//import net.citizensnpcs.api.npc.NPC;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public class EntityItem implements Property {

    // <--[property]
    // @object EntityTag
    // @name item
    // @input ItemTag
    // @description
    // An entity's item, which can be:
    // - the item represented and displayed by a dropped item.
    // - the item represented by a thrown trident.
    // - a throwable projectile's display item.
    // - an eye-of-ender's item, which is both displayed and dropped.
    // - a fireball's display item.
    // - an item display's display item.
    // -->

    public static boolean describes(ObjectTag object) {
        if (!(object instanceof EntityTag)) {
            return false;
        }
        Entity entity = ((EntityTag) object).getBukkitEntity();
        return entity instanceof ItemEntity
                || entity instanceof EnderMan
                || entity instanceof Fireball
                || entity instanceof ThrowableItemProjectile
                || entity instanceof EyeOfEnder; //todo 1.19
//                || (NMSHandler.getVersion().isAtLeast(NMSVersion.v1_19) && entity instanceof ItemDisplay);
    }

    public static EntityItem getFrom(ObjectTag entity) {
        if (!describes(entity)) {
            return null;
        }
        else {
            return new EntityItem((EntityTag) entity);
        }
    }

    public static final String[] handledMechs = new String[] {
            "item"
    };

    public EntityItem(EntityTag entity) {
        item = entity;
    }

    EntityTag item;

    public ItemTag getItem(boolean includeDeprecated, TagContext context) {
        if (isDroppedItem()) {
            return new ItemTag(getDroppedItem().getItem());
        }
        //todo remove deprecated
//        else if (includeDeprecated && isEnderman()) {
//            BukkitImplDeprecations.entityItemEnderman.warn(context);
//            BlockData data = getEnderman().getCarriedBlock();
//            if (data == null) {
//                return new ItemTag(Items.AIR);
//            }
//            return new ItemTag(data.getMaterial());
//        }
        else if (isFireball()) {
            return new ItemTag(getFireball().getItem());
        }
        else if (isThrowableProjectile()) {
            return new ItemTag(getThrowableProjectile().getItem());
        }
        else if (isEyeOfEnder()) {
            return new ItemTag(getEyeOfEnder().getItem());
        }
//        else if (isDisplay()) { // TODO: 1.19: when 1.19 is minimum, make a 'getDisplay'
//            return new ItemTag(((ItemDisplay) item.getBukkitEntity()).getItemStack());
//        }
        return null;
    }

    public boolean isDroppedItem() {
        return item.getBukkitEntity() instanceof ItemEntity;
    }

    public boolean isEnderman() {
        return item.getBukkitEntity() instanceof EnderMan;
    }

    public boolean isFireball() {
        return item.getBukkitEntity() instanceof Fireball;
    }

    public boolean isThrowableProjectile() {
        return item.getBukkitEntity() instanceof ThrowableItemProjectile;
    }

    public boolean isEyeOfEnder() {
        return item.getBukkitEntity() instanceof EyeOfEnder;
    }

//    public boolean isDisplay() {
//        return NMSHandler.getVersion().isAtLeast(NMSVersion.v1_19) && item.getBukkitEntity() instanceof Display;
//    }

    public ItemEntity getDroppedItem() {
        return ((ItemEntity) item.getBukkitEntity());
    }

    public EnderMan getEnderman() {
        return (EnderMan) item.getBukkitEntity();
    }

    public EyeOfEnder getEyeOfEnder() {
        return (EyeOfEnder) item.getBukkitEntity();
    }

    public ThrowableItemProjectile getThrowableProjectile() {
        return (ThrowableItemProjectile) item.getBukkitEntity();
    }

    public Fireball getFireball() {
        return (Fireball) item.getBukkitEntity();
    }

    @Override
    public String getPropertyString() {
        ItemTag item = getItem(false, null);
        if (item.getBukkitMaterial() != Items.AIR) {
            return item.identify();
        }
        return null;
    }

    @Override
    public String getPropertyId() {
        return "item";
    }

    public static void register() {
        PropertyParser.registerTag(EntityItem.class, ItemTag.class, "item", (attribute, object) -> {
            return object.getItem(true, attribute.context);
        });
    }

    @Override
    public void adjust(Mechanism mechanism) {
        if (mechanism.matches("item") && mechanism.requireObject(ItemTag.class)) {
            ItemStack itemStack = mechanism.valueAsType(ItemTag.class).getItemStack();
//            if (item.isCitizensNPC()) {
//                item.getDenizenNPC().getCitizen().data().setPersistent(NPC.Metadata.ITEM_ID, itemStack.getType().name());
//            }
            if (isDroppedItem()) {
                getDroppedItem().setItem(itemStack);
            }
            else if (isEnderman()) {
                BukkitImplDeprecations.entityItemEnderman.warn(mechanism.context);
                getEnderman().setCarriedBlock(Block.byItem(itemStack.getItem()).defaultBlockState());
            }
            else if (isFireball()) {
                getFireball().setItem(itemStack);
            }
            else if (isThrowableProjectile()) {
                getThrowableProjectile().setItem(itemStack);
            }
            else if (isEyeOfEnder()) {
                getEyeOfEnder().setItem(itemStack);
            }
            //todo 1.19
//            else if (isDisplay()) { // TODO: 1.19: when 1.19 is minimum, make a 'getDisplay'
//                ((ItemDisplay) item.getBukkitEntity()).setItemStack(itemStack);
//            }
        }
    }
}
