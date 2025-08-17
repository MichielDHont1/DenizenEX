package com.denizenscript.denizen.scripts.containers.core;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.denizen.nms.abstracts.ImprovedOfflinePlayer;
import com.denizenscript.denizen.objects.InventoryTag;
import com.denizenscript.denizen.utilities.Settings;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.objects.core.ScriptTag;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.*;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = "denizenex", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InventoryScriptHelper {

    public static boolean isPersonalSpecialInv(AbstractContainerMenu type) {
        return type instanceof AnvilMenu || type instanceof CraftingMenu;
    }

//    public static boolean isPersonalSpecialInv(Inventory inv) {
//        return isPersonalSpecialInv(inv.getType());
//    }

    public static Map<Container, InventoryTag> notedInventories = new HashMap<>();

    public static Map<AbstractContainerMenu, InventoryTag> notedMenus = new HashMap<>();

    public static HashMap<String, InventoryScriptContainer> inventoryScripts = new HashMap<>();

    private final static List<UUID> toClearOfflinePlayers = new ArrayList<>();

    public static void savePlayerInventories() {
        for (ImprovedOfflinePlayer player : ImprovedOfflinePlayer.offlinePlayers.values()) {
            if (player.inventory != null) { // TODO: optimize - remove inventories when no longer in use?
                player.setInventory(player.inventory);
            }
            if (player.enderchest != null) {
                player.setEnderChest(player.enderchest);
            }
            if (player.modified) {
                player.saveToFile();
            }
            if (player.timeLastLoaded + Settings.worldPlayerDataMaxCacheTicks < DenizenCore.currentTimeMonotonicMillis) {
                toClearOfflinePlayers.add(player.player);
            }
        }
        for (UUID id : toClearOfflinePlayers) {
            ImprovedOfflinePlayer.offlinePlayers.remove(id);
        }
        toClearOfflinePlayers.clear();
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ImprovedOfflinePlayer.invalidateNow(event.getPlayer().getUUID());
    }

    public static HashSet<ClickType> allowedClicks = new HashSet<>(Arrays.asList(

            ClickType.CLONE, ClickType.PICKUP, ClickType.PICKUP_ALL, ClickType.QUICK_CRAFT,
            ClickType.QUICK_MOVE, ClickType.SWAP, ClickType.THROW));

    public static boolean isGUI(AbstractContainerMenu inv) {
        InventoryTag inventory = InventoryTag.mirrorBukkitInventory(inv);
        if (inventory.getIdHolder() instanceof ScriptTag) {
            if (((InventoryScriptContainer) ((ScriptTag) inventory.getIdHolder()).getContainer()).gui) {
                return true;
            }
        }
        return false;
    }

    //todo
//    public static boolean isGUI(Container inv) {
//        InventoryTag inventory = InventoryTag.mirrorBukkitInventory(inv);
//        if (inventory.getIdHolder() instanceof ScriptTag) {
//            if (((InventoryScriptContainer) ((ScriptTag) inventory.getIdHolder()).getContainer()).gui) {
//                return true;
//            }
//        }
//        return false;
//    }

    //todo maybe requires mixins
//    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
//    public void onPlayerClicks( event) {
//        if (event.getRawSlot() >= event.getInventory().getSize() || event.getRawSlot() < 0) {
//            if (allowedClicks.contains(event.getClick())) {
//                return;
//            }
//        }
//        if (isGUI(event.getInventory())) {
//            event.setCancelled(true);
//            Bukkit.getScheduler().scheduleSyncDelayedTask(Denizen.getInstance(), () -> {
//                ((Player) event.getWhoClicked()).updateInventory();
//            }, 1);
//        }
//    }
//
//    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
//    public void onPlayerDrags(InventoryDragEvent event) {
//        if (isGUI(event.getInventory())) {
//            boolean anyInTop = false;
//            for (int slot : event.getRawSlots()) {
//                if (slot < event.getInventory().getSize()) {
//                    anyInTop = true;
//                    break;
//                }
//            }
//            if (anyInTop) {
//                event.setCancelled(true);
//            }
//        }
//    }
//
     //todo look into what this does
//    @SubscribeEvent(priority = EventPriority.HIGH)
//    public void onPlayerCloses(PlayerContainerEvent.Close event) {
//        if (isPersonalSpecialInv(event.getContainer()) && isGUI(event.getContainer())) {
//            event.getContainer().cl();
//        }
//    }
}
