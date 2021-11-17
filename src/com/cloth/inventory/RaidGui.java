package com.cloth.inventory;

import com.cloth.RaidTimers;
import com.cloth.raids.Raid;
import com.cryptomorin.xseries.XMaterial;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class RaidGui implements Gui {

    private Inventory inventory;
    private Raid raid;

    private final String inventoryName = "Raid";
    private int taskId = -1;

    public RaidGui(Raid raid) {
        // We need the Raid because our items will use the data from
        // the Raid object as its lore content.
        this.raid = raid;
        this.inventory = Bukkit.createInventory(null, 45, inventoryName);

        updateBorder();
        updateItems();
        updateFill();

        // Basic design to make the inventory look appealing.
        setItems(XMaterial.WHITE_STAINED_GLASS_PANE.parseItem(), 10, 16, 28, 34);
        setItems(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem(), 0, 8, 36, 44);

        RaidTimers.getInstance().registerListener(this);

        taskId = new BukkitRunnable() {
            @Override
            public void run() {
                updateItems();
            }
        }.runTaskTimer(RaidTimers.getInstance(), 0, 20).getTaskId();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(!event.getView().getTitle().equals(inventoryName))
            return;
        event.setCancelled(true);
    }

    public void updateFill() {
        for(int i = 0; i < inventory.getSize(); i++) {
            if(inventory.getItem(i) == null || inventory.getItem(i).getType() == XMaterial.AIR.parseMaterial())
                inventory.setItem(i, XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());
        }
    }

    public void updateBorder() {
        ItemStack item = XMaterial.WHITE_STAINED_GLASS_PANE.parseItem();
        for(int i = 0; i < inventory.getSize(); i++) {
            if(i % 9 == 0 || (i + 1) % 9 == 0 || i > 34 || i < 9)
                inventory.setItem(i, item);
        }
    }

    private void setItems(ItemStack item, int... slots) {
        for(int slot : slots) {
            inventory.setItem(slot, item);
        }
    }

    public void updateItems() {
        inventory.setItem(22, raid.getItem());
    }

    public void destroy() {
        HandlerList.unregisterAll(this);
        if(taskId != -1)
            Bukkit.getScheduler().cancelTask(taskId);
        // close gui for all viewers
        int size = inventory.getViewers().size();
        for(int i = size - 1; i >= 0; i--)
            inventory.getViewers().get(i).closeInventory();
        // cleanup
        inventory = null;
        raid = null;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }
}
