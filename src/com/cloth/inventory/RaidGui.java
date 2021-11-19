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

public class RaidGui extends Gui {

    private final Inventory inventory;
    private Raid raid;
    private int taskId = -1;

    public RaidGui(Raid raid, String name, int size) {
        super(name, size);

        this.inventory = getInventory();
        this.raid = raid;

        updateBorder();
        updateItems();
        updateFill();

        // Basic design to make the inventory look appealing.
        setItems(XMaterial.WHITE_STAINED_GLASS_PANE.parseItem(), 10, 16, 28, 34);
        setItems(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem(), 0, 8, 36, 44);

        taskId = new BukkitRunnable() {
            @Override
            public void run() {
                updateItems();
            }
        }.runTaskTimerAsynchronously(RaidTimers.getInstance(), 0, 20).getTaskId();
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

    public void updateItems() {
        inventory.setItem(22, raid.getItem());
    }

    @Override
    public void destroy() {
        super.destroy();
        if(taskId != -1)
            Bukkit.getScheduler().cancelTask(taskId);
        raid = null;
    }
}
