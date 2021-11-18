package com.cloth.inventory;

import com.cloth.RaidTimers;
import com.cloth.events.RaidEndEvent;
import com.cloth.events.RaidStartEvent;
import com.cloth.raids.Raid;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class RaidListGui extends Gui {

    private final Inventory inventory;

    public RaidListGui(String name, int size) {
        super(name, size);
        this.inventory = getInventory();
        updateBorder();
        updateItems();
    }

    @EventHandler
    public void onRaidStart(RaidStartEvent event) {
        updateItems();
    }

    @EventHandler
    public void onRaidEnd(RaidEndEvent event) {
        updateItems();
    }

    @Override
    public void updateBorder() {
        ItemStack item = XMaterial.WHITE_STAINED_GLASS_PANE.parseItem();
        for(int i = 0; i < inventory.getSize(); i++) {
            if(i > 52 || i < 9)
                inventory.setItem(i, item);
        }
    }

    @Override
    public void updateItems() {

        // clear the old items...
        for(int i = 10; i <= 52; i++) {
            inventory.setItem(i, new ItemStack(Material.AIR));
        }

        final List<Raid> raidList = RaidTimers.getApi().getRaidList();

        // add new raid items using new data...
        for(int i = 0, slot = 10; i < raidList.size() || slot <= 52; i++, slot++) {

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
}
