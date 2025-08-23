package com.denizenscript.denizen.utilities.inventory;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.denizen.objects.InventoryTag;
//import com.denizenscript.denizen.scripts.containers.core.InventoryScriptHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
//import org.bukkit.event.Listener;
import  net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.Mod;


import java.util.HashMap;
@Mod.EventBusSubscriber(modid = "denizenex", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InventoryTrackerSystem {

    public static HashMap<Long, InventoryTag> idTrackedInventories = new HashMap<>(512);

    public static HashMap<Long, InventoryTag> idTrackedMenus = new HashMap<>(512);

    public static long temporaryInventoryIdCounter = 0;

    public static long temporaryMenuIdCounter = 0;

    public static HashMap<Container, InventoryTag> temporaryInventoryLinks = new HashMap<>(512);

    public static HashMap<Container, InventoryTag> retainedInventoryLinks = new HashMap<>(512);

    public static HashMap<AbstractContainerMenu, InventoryTag> temporaryMenuLinks = new HashMap<>(512);

    public static HashMap<AbstractContainerMenu, InventoryTag> retainedMenuLinks = new HashMap<>(512);

    public static InventoryTag getTagFormFor(Inventory inventory) {
        if (inventory == null) {
            return null;
        }
        InventoryTag result = temporaryInventoryLinks.get(inventory);
        if (result != null) {
            return result;
        }
        return retainedInventoryLinks.get(inventory);
    }

    public static InventoryTag getTagFormFor(AbstractContainerMenu inventory) {
        if (inventory == null) {
            return null;
        }
        InventoryTag result = temporaryMenuLinks.get(inventory);
        if (result != null) {
            return result;
        }
        return retainedMenuLinks.get(inventory);
    }

    public static boolean isGenericTrackable(InventoryTag tagForm) {
        if (tagForm == null || tagForm.getIdType() == null) {
            return false;
        }
        return tagForm.getIdType().equals("generic") || tagForm.getIdType().equals("script");
    }

    //priority monitor
    @SubscribeEvent (priority = EventPriority.LOWEST)
    public static void onInventoryOpen(final PlayerContainerEvent.Open event) {
        if (event.isCanceled()) {
            return;
        }
        InventoryTag tagForm = getTagFormFor(event.getContainer());
        if (isGenericTrackable(tagForm)) {
            trackTemporaryInventory(event.getContainer(), tagForm);
            retainedMenuLinks.put(event.getContainer(), tagForm);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onInventoryClose(final PlayerContainerEvent.Close event) {
        AbstractContainerMenu inv = event.getContainer();
//        Bukkit.getScheduler().scheduleSyncDelayedTask(Denizen.getInstance(), () -> {
//            if (inv.getViewers().isEmpty()) {
                InventoryTag removed = retainedMenuLinks.remove(inv);
                if (removed != null && removed.uniquifier != null) {
                    idTrackedMenus.remove(removed.uniquifier);
                    temporaryMenuLinks.put(inv, removed);
                }
//            }
//        }, 1);
    }

    public static void trackTemporaryInventory(Container inventory, InventoryTag tagForm) {
        if (inventory == null || tagForm == null) {
            return;
        }
        if (!isGenericTrackable(tagForm)) {
            return;
        }
        //todo
//        if (InventoryScriptHelper.notedInventories.containsKey(inventory)) {
//            return;
//        }
        if (tagForm.uniquifier == null) {
            tagForm.uniquifier = temporaryInventoryIdCounter++;
        }
        if (!idTrackedInventories.containsKey(tagForm.uniquifier)) {
            idTrackedInventories.put(tagForm.uniquifier, tagForm);
            temporaryInventoryLinks.put(inventory, tagForm);
        }
    }

    public static void trackTemporaryInventory(AbstractContainerMenu inventory, InventoryTag tagForm) {
        if (inventory == null || tagForm == null) {
            return;
        }
        if (!isGenericTrackable(tagForm)) {
            return;
        }
        //todo
//        if (InventoryScriptHelper.notedMenus.containsKey(inventory)) {
//            return;
//        }
        if (tagForm.uniquifier == null) {
            tagForm.uniquifier = temporaryMenuIdCounter++;
        }
        if (!idTrackedMenus.containsKey(tagForm.uniquifier)) {
            idTrackedMenus.put(tagForm.uniquifier, tagForm);
            temporaryMenuLinks.put(inventory, tagForm);
        }
    }
//todo server tick event

    public static void setup() {
            if (idTrackedInventories.size() > 300) {
                idTrackedInventories.clear();
                for (InventoryTag temp : temporaryInventoryLinks.values()) {
                    idTrackedInventories.put(temp.uniquifier, temp);
                }
                for (InventoryTag retained : retainedInventoryLinks.values()) {
                    idTrackedInventories.put(retained.uniquifier, retained);
                    temporaryInventoryLinks.put(retained.getInventory(), retained);
                }
            }
            InventoryTrackerSystem.temporaryInventoryLinks.clear();

        if (idTrackedMenus.size() > 300) {
            idTrackedMenus.clear();
            for (InventoryTag temp : temporaryMenuLinks.values()) {
                idTrackedMenus.put(temp.uniquifier, temp);
            }
            for (InventoryTag retained : retainedMenuLinks.values()) {
                idTrackedMenus.put(retained.uniquifier, retained);
                temporaryMenuLinks.put(retained.getMenu(), retained);
            }
        }
        InventoryTrackerSystem.temporaryMenuLinks.clear();
//        }, 20, 20);
    }
}
