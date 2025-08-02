package com.denizenscript.denizen.utilities.entity;

import net.minecraft.world.entity.EntityType;

public class Gravity {

    // TODO once 1.20 is the minimum supported version can reference the enum directly

    public static double getGravity(EntityType<?> entityType) {
        if (entityType == EntityType.EXPERIENCE_BOTTLE) {
            return 0.157;
        }
        if (entityType == EntityType.ARROW) return 0.118;
        else if (entityType == EntityType.SNOWBALL) return 0.076;
        else if (entityType == EntityType.EGG) return 0.074;
        else return 0.115;
    }
}
