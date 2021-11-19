package com.cloth.inventory;

import com.cloth.RaidTimers;
import com.cloth.raids.Raid;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.List;

public class RaidListGui extends Gui {

    private final Inventory inventory;
    private int taskId = -1;

    // Arbitrary start and end to where our actual items will be...
    // The border will be around these values.
    final int FIRST_ITEM_SLOT = 18;
    final int LAST_ITEM_SLOT = 35;

    public RaidListGui(String name, int size) {
        super(name, size);
        this.inventory = getInventory();

        updateBorder();

        // cool design :)
        setItems(XMaterial.WHITE_STAINED_GLASS_PANE.parseItem(), 0, 8, 45, 53);

        taskId = new BukkitRunnable() {
            @Override
            public void run() {
                updateItems();
            }
        }.runTaskTimerAsynchronously(RaidTimers.getInstance(), 0, 20).getTaskId();
    }

    @Override
    public void updateBorder() {
        ItemStack black = XMaterial.BLACK_STAINED_GLASS_PANE.parseItem();
        ItemStack gray = XMaterial.GRAY_STAINED_GLASS_PANE.parseItem();
        for(int i = 0; i < inventory.getSize(); i++) {
            if (i > LAST_ITEM_SLOT || i < FIRST_ITEM_SLOT) {
                if(i < 9 || i > 44) {
                    inventory.setItem(i, gray);
                } else {
                    inventory.setItem(i, black);
                }
            }
        }
    }

    @Override
    public void updateItems() {
        // clear the old items...
        for(int i = FIRST_ITEM_SLOT; i <= LAST_ITEM_SLOT; i++)
            inventory.setItem(i, new ItemStack(Material.AIR));
        final List<Raid> raidList = RaidTimers.getApi()
                .getRaidList();
        // add new raid items using new data...
        for(int i = 0, slot = FIRST_ITEM_SLOT; i < raidList.size() && slot <= LAST_ITEM_SLOT; i++, slot++) {
            final Raid raid = raidList.get(i);
            final ItemStack item = raid.getItem();
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setDisplayName("§7Raid #" + i);
            item.setItemMeta(meta);
            inventory.setItem(slot, item);
        }
    }

    @Override
    public void updateFill() {
        // update the fill
    }

    @Override
    public void destroy() {
        super.destroy();
        if(taskId != -1)
            Bukkit.getScheduler().cancelTask(taskId);
    }
}
