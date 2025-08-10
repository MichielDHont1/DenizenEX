//todo not supported by forge, can only set the item
//package com.denizenscript.denizen.objects.properties.entity;
//
//import com.denizenscript.denizen.objects.EntityTag;
//import com.denizenscript.denizencore.objects.Mechanism;
//import com.denizenscript.denizencore.objects.core.ElementTag;
//import net.minecraft.world.entity.projectile.EyeOfEnder;
//
//public class EntityDropsItem extends EntityProperty<ElementTag> {
//
//    // <--[property]
//    // @object EntityTag
//    // @name drops_item
//    // @input ElementTag(Boolean)
//    // @description
//    // Whether an eye of ender drops an item when breaking or shatters.
//    // See <@link property EntityTag.item> for controlling an eye's item.
//    // -->
//
//    public static boolean describes(EntityTag entity) {
//        return entity.getBukkitEntity() instanceof EyeOfEnder;
//    }
//
//    @Override
//    public ElementTag getPropertyValue() {
//        return new ElementTag(as(EyeOfEnder.class).getDropItem());
//    }
//
//    @Override
//    public void setPropertyValue(ElementTag value, Mechanism mechanism) {
//        if (mechanism.requireBoolean()) {
//            as(EyeOfEnder.class).captureDrops(value.asBoolean());
//        }
//    }
//
//    @Override
//    public String getPropertyId() {
//        return "drops_item";
//    }
//
//    public static void register() {
//        autoRegister("drops_item", EntityDropsItem.class, ElementTag.class, false);
//    }
//}
