package com.denizenscript.denizen.utilities.inventory;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.denizen.objects.InventoryTag;
import com.denizenscript.denizen.scripts.containers.core.InventoryScriptHelper;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
//import org.bukkit.event.Listener;
import  net.minecraftforge.event.entity.player.PlayerContainerEvent;



import java.util.HashMap;

public class InventoryTrackerSystem implements Listener {

    public static HashMap<Long, InventoryTag> idTrackedInventories = new HashMap<>(512);

    public static long temporaryInventoryIdCounter = 0;

    public static HashMap<AbstractContainerMenu, InventoryTag> temporaryInventoryLinks = new HashMap<>(512);

    public static HashMap<AbstractContainerMenu, InventoryTag> retainedInventoryLinks = new HashMap<>(512);

    public static InventoryTag getTagFormFor(AbstractContainerMenu inventory) {
        if (inventory == null) {
            return null;
        }
        InventoryTag result = temporaryInventoryLinks.get(inventory);
        if (result != null) {
            return result;
        }
        return retainedInventoryLinks.get(inventory);
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
            retainedInventoryLinks.put(event.getInventory(), tagForm);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onInventoryClose(final PlayerContainerEvent.Close event) {
        Container inv = event.getInventory();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Denizen.getInstance(), () -> {
            if (inv.getViewers().isEmpty()) {
                InventoryTag removed = retainedInventoryLinks.remove(inv);
                if (removed != null && removed.uniquifier != null) {
                    idTrackedInventories.remove(removed.uniquifier);
                    temporaryInventoryLinks.put(inv, removed);
                }
            }
        }, 1);
    }

    public static void trackTemporaryInventory(Container inventory, InventoryTag tagForm) {
        if (inventory == null || tagForm == null) {
            return;
        }
        if (!isGenericTrackable(tagForm)) {
            return;
        }
        if (InventoryScriptHelper.notedInventories.containsKey(inventory)) {
            return;
        }
        if (tagForm.uniquifier == null) {
            tagForm.uniquifier = temporaryInventoryIdCounter++;
        }
        if (!idTrackedInventories.containsKey(tagForm.uniquifier)) {
            idTrackedInventories.put(tagForm.uniquifier, tagForm);
            temporaryInventoryLinks.put(inventory, tagForm);
        }
    }

    public static void setup() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Denizen.getInstance(), () -> {
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
        }, 20, 20);
        Bukkit.getPluginManager().registerEvents(new InventoryTrackerSystem(), Denizen.getInstance());
    }
}
