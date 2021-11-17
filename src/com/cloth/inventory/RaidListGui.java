package com.cloth.inventory;

import com.cloth.RaidTimers;
import com.cloth.raids.Raid;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RaidListGui implements Gui {

    private final Inventory inventory;
    private final String inventoryName = "Active Raids";

    public RaidListGui() {
        inventory = Bukkit.createInventory(null, 54, inventoryName);
        updateBorder();
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
        for(Raid raid : RaidTimers.getApi().getRaidList()) {

        }
    }

    @Override
    public void updateFill() {
        // update the fill
    }
}
