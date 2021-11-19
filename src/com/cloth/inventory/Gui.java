package com.cloth.inventory;

import com.cloth.RaidTimers;
import com.cryptomorin.xseries.XSound;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class Gui implements Listener {

    @Getter private Inventory inventory;
    @Getter private final String name;

    public Gui(String name, int size) {
        RaidTimers.getInstance().registerListener(this);

        this.name = name;
        this.inventory = Bukkit.createInventory(null, size, name);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(!event.getView().getTitle().equals(name))
            return;
        event.setCancelled(true);
    }

    public void setItems(ItemStack item, int... slots) {
        for(int slot : slots) {
            inventory.setItem(slot, item);
        }
    }

    public void destroy() {
        HandlerList.unregisterAll(this);
        // close gui for all viewers
        int size = inventory.getViewers().size();
        for(int i = size - 1; i >= 0; i--)
            inventory.getViewers().get(i).closeInventory();
        // cleanup
        inventory = null;
    }

    public void open(Player player) {
        player.openInventory(inventory);
        XSound.BLOCK_NOTE_BLOCK_PLING.play(player);
    }

    abstract void updateBorder();

    abstract void updateItems();

    abstract void updateFill();
}
