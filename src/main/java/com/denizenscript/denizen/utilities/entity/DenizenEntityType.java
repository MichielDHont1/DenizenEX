package com.denizenscript.denizen.utilities.entity;

import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizen.utilities.Settings;
import com.denizenscript.denizen.nms.NMSHandler;
//import com.denizenscript.denizen.nms.enums.CustomEntityType;
import com.denizenscript.denizen.nms.interfaces.CustomEntity;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.utilities.packets.NetworkInterceptHelper;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizen.utilities.BukkitImplDeprecations;
import com.denizenscript.denizen.utilities.Location;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DenizenEntityType {

    private static final Map<String, DenizenEntityType> registeredTypes = new HashMap<>();
    private final EntityType<?> bukkitEntityType;
    private final String name;
    private final String lowercaseName;
    private final double gravity;
//    public final CustomEntityType customEntityType;

    static {
        EntityType<?> type = null;
        for (ResourceLocation location : Registry.ENTITY_TYPE.keySet()) {
            type = Registry.ENTITY_TYPE.get(location);
            registeredTypes.put(type.toString(), new DenizenEntityType(type));
        }
    }

    // <--[language]
    // @name Denizen Entity Types
    // @group Useful Lists
    // @description
    // Along with the default EntityTypes <@link url https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/EntityType.html>,
    // Denizen also adds in a few altered entities:
    // - FAKE_ARROW: For use when you want an arrow to stay spawned at a location for any reason.
    // - FAKE_PLAYER: Spawns a fake player (non-Citizens NPC).
    //   Use with the mechanisms "name" and "skin" to alter the respective properties.
    // -->

    private DenizenEntityType(EntityType<?> entityType) {
        this.bukkitEntityType = entityType;
        this.name = entityType.toString();
        this.lowercaseName = CoreUtilities.toLowerCase(name);
        this.gravity = Gravity.getGravity(entityType);
//        this.customEntityType = null;
    }

    private DenizenEntityType(String name, Class<? extends CustomEntity> entityType) {
        this(name, entityType, 0.115);
    }

    private DenizenEntityType(String name, Class<? extends CustomEntity> entityType, double gravity) {
        EntityType<?> bukkitEntityType = null;
        //todo
//        if (entityType != null) {
//
//            for (EntityType<?> type : EntityType.values()) {
//                Class<? extends Entity> clazz = type.getEntityClass();
//                if (clazz != null && clazz.isAssignableFrom(entityType)) {
//                    bukkitEntityType = type;
//                    break;
//                }
//            }
//        }
        this.bukkitEntityType = bukkitEntityType;
        this.name = name.toUpperCase();
        this.lowercaseName = CoreUtilities.toLowerCase(name);
        this.gravity = gravity;
//        this.customEntityType = CustomEntityType.valueOf(name.toUpperCase());
    }

    public Entity spawnNewEntity(Location location, ArrayList<Mechanism> mechanisms, String scriptName, MobSpawnType reason) {
        try {
            if (name.equals("DROPPED_ITEM")) {
                ItemStack itemStack = new ItemStack(Items.STONE);
                for (Mechanism mechanism : mechanisms) {
                    if (mechanism.matches("item") && mechanism.requireObject(ItemTag.class)) {
                        itemStack = mechanism.valueAsType(ItemTag.class).getItemStack();
                        break;
                    }
                }
                ItemEntity toSpawn = new ItemEntity(EntityType.ITEM, location.getWorld());
                toSpawn.setPos(location.toVector());
                toSpawn.setItem(itemStack);
                location.getWorld().addFreshEntity(toSpawn);
                return toSpawn;
            }
            //todo handle other entity types
//            else {
//                ItemEntity toSpawn = new Entity(getBukkitEntityType(), location.getWorld());
//                toSpawn.setPos(location.toVector());
//                toSpawn.setItem(itemStack);
//                location.getWorld().addFreshEntity(toSpawn);
//                return toSpawn;
//                return SpawnEntityHelper.spawn(location, bukkitEntityType, mechanisms, scriptName, reason);
//            }
//            else if (!isCustom()) {
//                return SpawnEntityHelper.spawn(location, bukkitEntityType, mechanisms, scriptName, reason);
//            }
//            else {
//                switch (customEntityType) {
//                    case FAKE_ARROW:
//                        return NMSHandler.customEntityHelper.spawnFakeArrow(location);
//                    case FAKE_PLAYER:
//                        if (Settings.packetInterception()) {
//                            String name = null;
//                            String skin = null;
//                            String blob = null;
//                            for (Mechanism mechanism : new ArrayList<>(mechanisms)) {
//                                if (mechanism.matches("name")) {
//                                    name = mechanism.getValue().asString();
//                                    mechanisms.remove(mechanism);
//                                }
//                                else if (mechanism.matches("skin")) {
//                                    skin = mechanism.getValue().asString();
//                                    mechanisms.remove(mechanism);
//                                }
//                                else if (mechanism.matches("skin_blob")) {
//                                    blob = mechanism.getValue().asString();
//                                    mechanisms.remove(mechanism);
//                                }
//                                if (name != null && (skin != null || blob != null)) {
//                                    break;
//                                }
//                            }
//                            NetworkInterceptHelper.enable();
//                            return NMSHandler.customEntityHelper.spawnFakePlayer(location, name, skin, blob, true);
//                        }
//                        break;
//                    case ITEM_PROJECTILE:
//                        BukkitImplDeprecations.itemProjectile.warn();
//                        ItemStack itemStack = new ItemStack(Material.STONE);
//                        for (Mechanism mechanism : mechanisms) {
//                            if (mechanism.matches("item") && mechanism.requireObject(ItemTag.class)) {
//                                itemStack = mechanism.valueAsType(ItemTag.class).getItemStack();
//                            }
//                        }
//                        return NMSHandler.customEntityHelper.spawnItemProjectile(location, itemStack);
//                }
//            }
        }
        catch (Exception e) {
            Debug.echoError(e);
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public String getLowercaseName() {
        return this.lowercaseName;
    }

    public double getGravity() {
        return this.gravity;
    }

    public EntityType getBukkitEntityType() {
        return bukkitEntityType;
    }

    public static void registerEntityType(String name, Class<? extends CustomEntity> entityType) {
        registeredTypes.put(name.toUpperCase(), new DenizenEntityType(name, entityType));
    }

    public static boolean isRegistered(String name) {
        return registeredTypes.containsKey(name.toUpperCase());
    }

    public static DenizenEntityType getByName(String name) {
        return registeredTypes.get(name.toUpperCase());
    }

    public static DenizenEntityType getByEntity(Entity entity) {
        //todo
//        if (entity instanceof CustomEntity) {
//            return getByName(((CustomEntity) entity).getBukkitEntityType);
//        }
//        else {
            return getByName(entity.getType().toString());
//        }
    }

//    public boolean isCustom() {
//        return customEntityType != null;
//    }

    @Override
    public String toString() {
        return getName();
    }
}
