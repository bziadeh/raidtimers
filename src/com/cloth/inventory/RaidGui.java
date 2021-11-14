package com.cloth.inventory;

import com.cloth.RaidTimers;
import com.cloth.context.RaidContext;
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

public class RaidGui implements Listener {

    private Inventory inventory;
    private RaidContext context;

    private final String inventoryName = "Raid";
    private int taskId = -1;

    public RaidGui(RaidContext context) {
        // We need the RaidContext because our items will use the data from
        // the RaidContext object as its lore content.
        this.context = context;
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

    private void updateFill() {
        for(int i = 0; i < inventory.getSize(); i++) {
            if(inventory.getItem(i) == null || inventory.getItem(i).getType() == XMaterial.AIR.parseMaterial())
                inventory.setItem(i, XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());
        }
    }

    private void updateBorder() {
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
        // time elapsed in minutes since the last explosion
        long elapsed = (System.currentTimeMillis() - context.getLastExplosion());
        // getting online players for both factions
        int defenderOnline = context.getDefender().getOnlinePlayers().size();
        int attackerOnline = context.getAttacker().getOnlinePlayers().size();
        // create our center item with the data added
        GuiItem centerItem = new GuiItem(XMaterial.TNT.parseMaterial())
                .setName("&8&l[&4&l!&8&l] &c&lYOU ARE CURRENTLY IN A RAID")
                .setLore("",
                        "&8&lRAIDING: &f" + context.getAttacker().getTag() + " &7(" + attackerOnline + " Online)",
                        "&8&lDEFENDING: &f" + context.getDefender().getTag() + " &7(" + defenderOnline + " Online)",
                        "",
                        "&7The faction actively raiding must fire TNT once every",
                        "&715 minutes to remain in raid.",
                        "",
                        "&7While this raid is active, the defending faction",
                        "&7will be unable to mine spawners or regen walls.",
                        "",
                        "&cLast Explosion: " + getTimeElapsed(elapsed) + " ago");
        inventory.setItem(22, centerItem.getItem());
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
        context = null;
    }

    private String getTimeElapsed(long millis) {
        return DurationFormatUtils.formatDurationWords(millis,
                true,
                false);
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }
}
